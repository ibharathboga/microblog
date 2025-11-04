import { createContext, useContext, useEffect, useState, ReactNode, useCallback } from 'react';
import { fetchEventSource, EventSourceMessage } from '@microsoft/fetch-event-source';
import { toast } from 'sonner';
import { useAuth } from './AuthContext';
import { notificationsAPI } from '@/lib/api';

interface Notification {
  id: string;
  type: string;
  actorUsername: string;
  actorId: string;
  postId?: string;
  createdAt: string;
  isRead: boolean;
}

interface NotificationContextType {
  unreadCount: number;
  notifications: Notification[];
  refreshNotifications: () => Promise<void>;
  markAllAsRead: () => Promise<void>;
  recentNotification: Notification | null;
}

const NotificationContext = createContext<NotificationContextType>({
  unreadCount: 0,
  notifications: [],
  refreshNotifications: async () => { },
  markAllAsRead: async () => { },
  recentNotification: null,
});

export function NotificationProvider({ children }: { children: ReactNode }) {
  const { user } = useAuth();
  const [unreadCount, setUnreadCount] = useState(0);
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [connected, setConnected] = useState(false);
  const [subscription, setSubscription] = useState<AbortController | null>(null);
  const [recentNotification, setRecentNotification] = useState<Notification | null>(null);

  const getNotificationMessage = (notification: Notification): string => {
    switch (notification.type) {
      case 'LIKE':
        return `@${notification.actorUsername} liked your post`;
      case 'UNLIKE':
        return `@${notification.actorUsername} unliked your post`;
      case 'FOLLOW':
        return `@${notification.actorUsername} started following you`;
      case 'UNFOLLOW':
        return `@${notification.actorUsername} unfollowed you`;
      case 'NEW_POST':
        return `@${notification.actorUsername} created a new post`;
      default:
        return 'New notification';
    }
  };

  const refreshNotifications = useCallback(async () => {
    if (!user) return;
    try {
      const [allNotifications, count] = await Promise.all([
        notificationsAPI.getAll(),
        notificationsAPI.getUnreadCount(),
      ]);
      setNotifications(allNotifications);
      setUnreadCount(count);
    } catch (error) {
      console.error('Failed to refresh notifications:', error);
    }
  }, [user]);

  const markAllAsRead = useCallback(async () => {
    try {
      await notificationsAPI.markAllAsRead();
      setNotifications((prev) => prev.map((n) => ({ ...n, isRead: true })));
      setUnreadCount(0);
      toast.success('All notifications marked as read');
    } catch (error) {
      toast.error('Failed to mark notifications as read');
    }
  }, []);

  const connectSSE = useCallback(async () => {
    if (connected) {
      console.log('Already connected to notification stream.');
      return;
    }

    const controller = new AbortController();
    setSubscription(controller);

    try {
      if (!user) {
        console.log('No authenticated user found.');
        return;
      }

      const token = await user.getIdToken();
      const url = 'http://localhost:8080/notifications/subscribe';
      console.log(`Connecting to SSE at ${url}`);

      await fetchEventSource(url, {
        openWhenHidden: true,
        headers: { Authorization: `Bearer ${token}` },
        signal: controller.signal,

        async onopen(response) {
          if (response.ok) {
            console.log('SSE connection established successfully.');
            setConnected(true);
          } else {
            console.error(`Connection failed with status ${response.status}`);
            throw new Error('SSE connection failed');
          }
        },

        onmessage(event: EventSourceMessage) {
          try {
            const raw = JSON.parse(event.data);
            console.log('Notification event:', raw);

            const notification: Notification = {
              id: raw.id,
              type: raw.type,
              actorUsername: raw.actorUsername,
              actorId: raw.actorId,
              postId: raw.postId,
              createdAt: new Date(raw.createdAt).toISOString(),
              isRead: false,
            };

            setNotifications((prev) => [notification, ...prev]);
            setUnreadCount((prev) => prev + 1);
            toast.info(getNotificationMessage(notification));
            setRecentNotification(notification);
          } catch (error) {
            console.log(error)
            console.log('[Raw Event]', event.data);
          }
        },

        onerror(err) {
          console.error('SSE error:', err);
          setConnected(false);
          throw err;
        },

        onclose() {
          console.log('SSE connection closed.');
          setConnected(false);
        },
      });
    } catch (err: any) {
      console.error('SSE error:', err?.message || err);
      setConnected(false);
    }
  }, [user, connected]);

  const disconnectSSE = useCallback(() => {
    if (subscription) {
      console.log('Disconnecting SSE...');
      subscription.abort();
      setConnected(false);
      setSubscription(null);
    } else {
      console.log('No active SSE connection.');
    }
  }, [subscription]);

  useEffect(() => {
    if (!user) {
      disconnectSSE();
      setNotifications([]);
      setUnreadCount(0);
      return;
    }

    refreshNotifications();
    connectSSE();

    return () => {
      if (subscription) subscription.abort();
      setConnected(false);
    };
  }, [user]);

  return (
    <NotificationContext.Provider
      value={{
        unreadCount,
        notifications,
        refreshNotifications,
        markAllAsRead,
        recentNotification
      }}
    >
      {children}
    </NotificationContext.Provider>
  );
}

export const useNotifications = () => useContext(NotificationContext);
