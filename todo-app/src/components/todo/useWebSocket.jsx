// src/services/WebSocketService.js

import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

let client;

export const connectWebSocket = (sharelink, onMessageReceived) => {
    const socket = new SockJS('http://localhost:8080/ws'); // Use your backend URL
    client = new Client({
        webSocketFactory: () => socket,
        onConnect: () => {
            console.log('Connected to WebSocket');
            client.subscribe(`/topic/comments/${sharelink}`, message => {
                onMessageReceived(JSON.parse(message.body));
            });
        },
        onDisconnect: () => {
            console.log('Disconnected from WebSocket');
        },
        onStompError: (frame) => {
            console.error(`WebSocket error: ${frame.body}`);
        }
    });

    client.activate();
};

export const disconnectWebSocket = () => {
    if (client) {
        client.deactivate();
    }
};

export const sendComment = (sharelink, commentData) => {
    if (client && client.connected) {
        client.publish({
            destination: `/app/comments/${sharelink}`,
            body: JSON.stringify(commentData)
        });
    }
};
