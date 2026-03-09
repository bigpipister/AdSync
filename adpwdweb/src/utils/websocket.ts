import { Client } from "@stomp/stompjs";
import { getToken } from "@/utils/auth";

class WebSocketManager {
  private client: Client | null = null;
  private messageHandlers: Map<string, ((message: string) => void)[]> = new Map();
  private reconnectAttempts = 0;
  private maxReconnectAttempts = 3; // 自定義最大重試次數
  private reconnectDelay = 5000; // 重試延遲（單位：毫秒）

  // 初始化 WebSocket 客戶端
  setupWebSocket() {
    const endpoint = import.meta.env.VITE_APP_WS_ENDPOINT;

    // 如果沒有配置 WebSocket 端點或顯式關閉，直接返回
    if (!endpoint) {
      console.log("WebSocket 已被禁用，如需打開請在配置文件中配置 VITE_APP_WS_ENDPOINT");
      return;
    }

    if (this.client && this.client.connected) {
      console.log("客戶端已存在並且連接正常");
      return this.client;
    }

    this.client = new Client({
      brokerURL: endpoint,
      connectHeaders: {
        Authorization: getToken(),
      },
      heartbeatIncoming: 30000,
      heartbeatOutgoing: 30000,
      reconnectDelay: 0, // 設置為 0 禁用重連
      onConnect: () => {
        console.log(`連接到 WebSocket 服務器: ${endpoint}`);
        this.reconnectAttempts = 0; // 重置重連計數
        this.messageHandlers.forEach((handlers, topic) => {
          handlers.forEach((handler) => {
            this.subscribeToTopic(topic, handler);
          });
        });
      },
      onStompError: (frame) => {
        console.error(`連接錯誤: ${frame.headers["message"]}`);
        console.error(`錯誤詳情: ${frame.body}`);
      },
      onDisconnect: () => {
        console.log(`WebSocket 連接已斷開: ${endpoint}`);
        this.reconnectAttempts++;
        if (this.reconnectAttempts < this.maxReconnectAttempts) {
          console.log(`正在嘗試重連... 嘗試次數: ${this.reconnectAttempts}`);
        } else {
          console.log("重連次數已達上限，停止重連");
          this.client?.deactivate();
        }
      },
    });

    this.client.activate();
  }

  // 訂閱主題
  public subscribeToTopic(topic: string, onMessage: (message: string) => void) {
    console.log(`正在訂閱主題: ${topic}`);
    if (!this.client || !this.client.connected) {
      this.setupWebSocket();
    }

    if (this.messageHandlers.has(topic)) {
      this.messageHandlers.get(topic)?.push(onMessage);
    } else {
      this.messageHandlers.set(topic, [onMessage]);
    }

    if (this.client?.connected) {
      this.client.subscribe(topic, (message) => {
        const handlers = this.messageHandlers.get(topic);
        handlers?.forEach((handler) => handler(message.body));
      });
    }
  }

  // 斷開 WebSocket 連接
  public disconnect() {
    if (this.client) {
      console.log("斷開 WebSocket 連接");
      this.client.deactivate();
      this.client = null;
    }
  }
}

export default new WebSocketManager();
