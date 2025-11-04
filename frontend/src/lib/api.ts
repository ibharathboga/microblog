import { fetchEventSource, EventSourceMessage } from '@microsoft/fetch-event-source';
import { auth } from './firebase';

const API_BASE_URL = 'http://localhost:8080';

async function getAuthToken(): Promise<string | null> {
  const user = auth.currentUser;
  if (!user) return null;
  return await user.getIdToken();
}

async function fetchWithAuth(endpoint: string, options: RequestInit = {}) {
  const token = await getAuthToken();
  const headers: HeadersInit = {
    'Content-Type': 'application/json',
    ...(token && { Authorization: `Bearer ${token}` }),
    ...options.headers,
  };

  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    ...options,
    headers,
  });

  if (!response.ok) {
    const error = await response.json().catch(() => ({ message: 'Request failed' }));
    throw new Error(error.message || `HTTP ${response.status}`);
  }

  if (response.status === 204) {
    return null;
  }

  const contentType = response.headers.get('Content-Type');
  if (!contentType || !contentType.includes('application/json')) {
    return null;
  }

  return response.json();
}

// Auth API
export const authAPI = {
  register: (data: { username: string; email: string; password: string }) =>
    fetchWithAuth('/auth/register', {
      method: 'POST',
      body: JSON.stringify(data),
    }),

  getCurrentUser: () => fetchWithAuth('/auth/me'),
};

// Users API
export const usersAPI = {
  getProfile: (username: string) => fetchWithAuth(`/users/info/${username}`),
  searchUsers: (query: string) => fetchWithAuth(`/users/search/${query}`),
};

// Posts API
export const postsAPI = {
  createPost: (content: string) =>
    fetchWithAuth('/posts', {
      method: 'POST',
      body: JSON.stringify({ content }),
    }),

  getUserPosts: (username: string) => fetchWithAuth(`/posts/${username}`),

  deletePost: (postId: string) =>
    fetchWithAuth(`/posts/${postId}`, { method: 'DELETE' }),
};

// Feed API
export const feedAPI = {
  getPublicFeed: (page = 0, size = 10) =>
    fetchWithAuth(`/feed/public?page=${page}&size=${size}`),

  getFollowingFeed: (page = 0, size = 10) =>
    fetchWithAuth(`/feed/following?page=${page}&size=${size}`),
};

// Follows API
export const followsAPI = {
  followUser: (userId: string) =>
    fetchWithAuth(`/follows/${userId}`, { method: 'POST' }),

  unfollowUser: (userId: string) =>
    fetchWithAuth(`/follows/${userId}`, { method: 'DELETE' }),

  getFollowers: (userId: string) => fetchWithAuth(`/follows/followers/${userId}`),

  getFollowing: (userId: string) => fetchWithAuth(`/follows/followees/${userId}`),
};

// Likes API
export const likesAPI = {
  likePost: (postId: string) =>
    fetchWithAuth(`/likes/${postId}`, { method: 'POST' }),

  unlikePost: (postId: string) =>
    fetchWithAuth(`/likes/${postId}`, { method: 'DELETE' }),

  postsLikedByUser: (username: string) => fetchWithAuth(`/likes/user/${username}`)
};

// Notifications API
export const notificationsAPI = {
  getAll: () => fetchWithAuth('/notifications'),

  getUnread: () => fetchWithAuth('/notifications/unread'),

  getUnreadCount: async (): Promise<number> => {
    const response = await fetchWithAuth('/notifications/unread/count');
    return response as number;
  },

  markAsRead: (notificationId: string) =>
    fetchWithAuth(`/notifications/${notificationId}/read`, { method: 'POST' }),

  markAllAsRead: () =>
    fetchWithAuth('/notifications/read-all', { method: 'POST' }),
};

// SSE for real-time updates
// based on the previous provided code
// refactor this code to use fetchsource


interface SSEConnectionOptions {
  onMessage: (data: any) => void;
  onError?: (error: unknown) => void;
  onOpen?: () => void;
  onClose?: () => void;
}

export async function createSSEConnection(
  endpoint: string,
  { onMessage, onError, onOpen, onClose }: SSEConnectionOptions
): Promise<AbortController> {
  const user = auth.currentUser;
  if (!user) {
    throw new Error('No authenticated user found.');
  }

  const token = await user.getIdToken();
  const controller = new AbortController();
  const url = `${API_BASE_URL}${endpoint}`;

  fetchEventSource(url, {
    openWhenHidden: true,
    headers: { Authorization: `Bearer ${token}` },
    signal: controller.signal,
    async onopen(response) {
      if (response.ok) {
        onOpen?.();
      } else {
        const error = `Failed to connect: ${response.status}`;
        onError?.(error);
        throw new Error(error);
      }
    },
    onmessage(event: EventSourceMessage) {
      try {
        const data = JSON.parse(event.data);
        onMessage(data);
      } catch (err) {
        console.error('Failed to parse SSE message:', err);
      }
    },
    onerror(err) {
      console.error('SSE error:', err);
      onError?.(err);
      onClose?.();
      throw err;
    },
    onclose() {
      onClose?.();
      console.log('SSE connection closed.');
    },
  });

  return controller;
}