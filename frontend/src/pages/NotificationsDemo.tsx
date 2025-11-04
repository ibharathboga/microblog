import React, { useEffect, useState, useCallback, useRef } from 'react';
import { fetchEventSource, EventSourceMessage } from '@microsoft/fetch-event-source';
import { getAuth } from 'firebase/auth';
import axios from 'axios';

export default function NotificationsDemoPage() {
  const [messages, setMessages] = useState<string[]>([]);
  const [connected, setConnected] = useState(false);
  const [subscription, setSubscription] = useState<AbortController | null>(null);
  const auth = getAuth();

  const appendMessage = useCallback((msg: string) => {
    setMessages(prev => [...prev, msg]);
  }, []);

  const connectSSE = useCallback(async () => {

    // console.log("c");
    if (connected) {
      appendMessage('Already connected.');
      return;
    }

    const controller = new AbortController();
    setSubscription(controller);

    try {
      const user = auth.currentUser;
      if (!user) {
        appendMessage('No authenticated user found.');
        return;
      }

      const token = await user.getIdToken();
      const url = 'http://localhost:8080/notifications/subscribe';
      appendMessage(`Connecting to SSE at ${url}`);

      await fetchEventSource(url, {
        headers: { Authorization: `Bearer ${token}` },
        signal: controller.signal,

        async onopen(response) {
          if (response.ok) {
            appendMessage('SSE connection established successfully.');
            setConnected(true);
          } else {
            appendMessage(`Connection failed with status ${response.status}`);
            throw new Error('SSE connection failed');
          }
        },

        onmessage(event: EventSourceMessage) {
          try {
            const parsed = JSON.parse(event.data);
            appendMessage(`[Message] ${JSON.stringify(parsed)}`);
          } catch {
            appendMessage(`[Raw Event] ${event.data}`);
          }
        },

        onerror(err) {
          appendMessage(`[Error] ${err?.message || err}`);
          setConnected(false);
          throw err;
        },

        onclose() {
          appendMessage('SSE connection closed.');
          setConnected(false);
        },
      });
    } catch (err: any) {
      appendMessage(`SSE error: ${err?.message || err}`);
      setConnected(false);
    }
  }, [auth, appendMessage, connected]);

  const disconnectSSE = useCallback(() => {
    if (subscription) {
      appendMessage('Disconnecting SSE...');
      subscription.abort();
      setConnected(false);
      setSubscription(null);
    } else {
      appendMessage('No active connection to stop.');
    }
  }, [subscription, appendMessage]);

  const sendPing = useCallback(async () => {
    try {
      const user = auth.currentUser;
      if (!user) {
        appendMessage('No authenticated user found for ping.');
        return;
      }

      const token = await user.getIdToken();
      const res = await axios.get('http://localhost:8080/notifications/ping', {
        headers: { Authorization: `Bearer ${token}` },
      });

      appendMessage(`Ping successful: ${JSON.stringify(res.data)}`);
    } catch (err: any) {
      appendMessage(`Ping error: ${err?.message || err}`);
    }
  }, [auth, appendMessage]);


  useEffect(() => {
    return () => {
      if (subscription) subscription.abort();
    };
  }, [subscription]);

  return (
    <div style={{ padding: 24, fontFamily: 'Arial' }}>
      <h2>Notifications Page Demo (React)</h2>
      <div style={{ marginBottom: 12 }}>
        <button onClick={connectSSE} disabled={connected} style={{ marginRight: 8 }}>
          Start SSE
        </button>
        <button onClick={disconnectSSE} disabled={!connected} style={{ marginRight: 8 }}>
          Stop
        </button>
        <button onClick={sendPing}>Send Ping</button>
      </div>

      <div style={{ border: '1px solid #ccc', padding: 12, height: 300, overflowY: 'auto' }}>
        {messages.map((msg, i) => (
          <div key={i}>{msg}</div>
        ))}
      </div>
    </div>
  );
}
