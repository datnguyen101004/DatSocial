import React, { useEffect, useState } from "react";
import axios from "axios";
import "./Css/Chat.css";
import SockJS from "sockjs-client";  // Import SockJS
import { Client } from "@stomp/stompjs";  // Import STOMP Client

const Chat = () => {
  const [friends, setFriends] = useState([]); // Danh sách bạn bè
  const [friendError, setFriendError] = useState(""); // Lỗi bạn bè
  const [chatError, setChatError] = useState(""); // Lỗi chat
  const [selectedFriend, setSelectedFriend] = useState(null); // Bạn bè đã chọn
  const [messages, setMessages] = useState([]); // Tin nhắn
  const [newMessage, setNewMessage] = useState(""); // Tin nhắn mới
  const token = localStorage.getItem("jwtToken");
  const [roomId, setRoomId] = useState("");

  // Kết nối WebSocket
  const [stompClient, setStompClient] = useState(null);

  // Fetch danh sách bạn bè
  useEffect(() => {
    const fetchFriends = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/v1/user/friend/all", {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });
        if (response.data.status === 200) {
          setFriends(response.data.data);
        } else {
          setFriendError(response.data.message || "Error fetching friends.");
        }
      } catch (err) {
        setFriendError("Network error or server unavailable.");
      }
    };

    fetchFriends();
  }, [token]);

  // Fetch tin nhắn
  const fetchMessages = async (id) => {
    try {
      const response = await axios.post(`http://localhost:8080/api/v1/rooms/messages`, {
        "receiverId": id,
      }, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (response.data.status === 200) {
        setRoomId(response.data.data.roomId);
        setMessages(response.data.data.messages);
      } else {
        setChatError("Error fetching messages.");
      }
    } catch (err) {
      setChatError("Network error while fetching messages.");
    }
  };

  // Xử lý khi click vào bạn bè
  const handleFriendClick = (friend) => {
    setSelectedFriend(friend);
    fetchMessages(friend.id);
  };

  // Kết nối WebSocket khi roomId thay đổi
  useEffect(() => {
    if (!roomId) return; // Không kết nối nếu chưa có roomId

    const socket = new SockJS("http://localhost:8080/chat");
    const client = new Client({
      webSocketFactory: () => socket,
      connectHeaders: {
        Authorization: `Bearer ${token}`,
        withCredentials: true, // Đảm bảo credentials được gửi cùng yêu cầu
      },
      onConnect: () => {
        console.log("WebSocket connected!");
        // Đăng ký nhận tin nhắn cho roomId
        client.subscribe(`/topic/${roomId}`, (message) => {
          console.log("Received message:", message);
          setMessages((prevMessages) => [
            ...prevMessages,
            JSON.parse(message.body),
          ]);
        });
      },
      onStompError: (frame) => {
        console.error("WebSocket error:", frame);
      },
    });

    client.activate();
    setStompClient(client); // Lưu stompClient để có thể hủy kết nối sau này

    // Dọn dẹp WebSocket khi component bị unmount hoặc roomId thay đổi
    return () => {
      if (client) {
        client.deactivate();
      }
    };
  }, [roomId, token]); // Chỉ khi roomId thay đổi mới kết nối WebSocket

  // Gửi tin nhắn qua WebSocket
  const handleSendMessage = () => {
    if (!newMessage.trim()) return;
    if (!stompClient || !stompClient.connected) return; // Kiểm tra nếu stompClient chưa được kết nối

    // Gửi tin nhắn qua WebSocket
    stompClient.publish({
      destination: `/app/sendMessage/${roomId}`,
      body: JSON.stringify({ content: newMessage }),
    });

    // Thêm tin nhắn vào UI
    setMessages((prevMessages) => [
      ...prevMessages,
      { content: newMessage, fromMe: true },
    ]);
    setNewMessage("");
  };

  return (
    <div className="chat-container">
      {/* Sidebar */}
      <div className="chat-sidebar">
        <h3>Friends</h3>
        {friendError ? (
          <p className="error-message">{friendError}</p>
        ) : (
          <ul className="friend-list">
            {friends.length > 0 ? (
              friends.map((friend) => (
                <li
                  key={friend.id}
                  className={`friend-item ${selectedFriend?.id === friend.id ? "active" : ""}`}
                  onClick={() => handleFriendClick(friend)}
                >
                  {friend.fullName}
                </li>
              ))
            ) : (
              <p>No friends available.</p>
            )}
          </ul>
        )}
      </div>

      {/* Chat Section */}
      <div className="chat-main">
        {selectedFriend ? (
          <div className="chat-content">
            <h3>Chat with {selectedFriend.fullName}</h3>
            {chatError ? (
              <p className="error-message">{chatError}</p>
            ) : (
              <div className="messages">
                {messages.length > 0 ? (
                  messages.map((msg, index) => (
                    <div key={index} className={`message ${msg.fromMe ? "from-me" : "from-them"}`}>
                      {msg.content}
                    </div>
                  ))
                ) : (
                  <p>No messages yet.</p>
                )}
              </div>
            )}
            <div className="chat-input">
              <input
                type="text"
                value={newMessage}
                onChange={(e) => setNewMessage(e.target.value)}
                placeholder="Type a message..."
              />
              <button onClick={handleSendMessage}>Send</button>
            </div>
          </div>
        ) : (
          <p>Select a friend to start a chat.</p>
        )}
      </div>
    </div>
  );
};

export default Chat;
