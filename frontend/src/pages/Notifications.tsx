import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Layout } from '@/components/Layout';
import { Bell } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { useNotifications } from '@/contexts/NotificationContext';

interface Notification {
  id: string;
  type: string;
  actorUsername: string;
  actorId: string;
  postId?: string;
  createdAt: string;
  isRead: boolean;
}

export default function Notifications() {
  const { notifications, refreshNotifications, markAllAsRead } = useNotifications();
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const loadData = async () => {
      await refreshNotifications();
      setLoading(false);
    };
    loadData();
  }, []);

  const handleMarkAllAsRead = async () => {
    await markAllAsRead();
  };

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

  const getNotificationLink = (notification: Notification): string => {
    if (notification.postId) {
      return `/user/${notification.actorUsername}`;
    }
    return `/user/${notification.actorUsername}`;
  };

  if (loading) {
    return (
      <Layout>
        <div className="flex min-h-screen items-center justify-center">
          <div className="h-8 w-8 animate-spin rounded-full border-4 border-primary border-t-transparent" />
        </div>
      </Layout>
    );
  }

  return (
    <Layout>
      <div className="border-x border-border min-h-screen">
        <div className="sticky top-0 z-10 border-b border-border bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
          <div className="flex items-center justify-between p-4">
            <h1 className="text-xl font-bold">Notifications</h1>
            {notifications.some((n) => !n.isRead) && (
              <Button variant="ghost" size="sm" onClick={handleMarkAllAsRead}>
                Mark all as read
              </Button>
            )}
          </div>
        </div>

        <div className="divide-y divide-border">
          {notifications.length === 0 ? (
            <div className="flex flex-col items-center justify-center py-16 text-center">
              <Bell className="h-12 w-12 text-muted-foreground/50 mb-4" />
              <p className="text-lg text-muted-foreground">No notifications yet</p>
              <p className="text-sm text-muted-foreground">
                You'll see notifications here when someone interacts with your posts
              </p>
            </div>
          ) : (
            notifications.map((notification) => (
              <Link
                key={notification.id}
                to={getNotificationLink(notification)}
                className={`block p-4 hover:bg-muted/50 transition-colors ${
                  !notification.isRead ? 'bg-primary/5' : ''
                }`}
              >
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <p className="font-medium">{getNotificationMessage(notification)}</p>
                    <p className="text-xs text-muted-foreground mt-1">
                      {new Date(notification.createdAt).toLocaleString()}
                    </p>
                  </div>
                  {!notification.isRead && (
                    <div className="h-2 w-2 rounded-full bg-primary ml-2 mt-2" />
                  )}
                </div>
              </Link>
            ))
          )}
        </div>
      </div>
    </Layout>
  );
}
