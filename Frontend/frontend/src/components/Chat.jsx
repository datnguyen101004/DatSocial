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
  const [roomId, setRoomId] = useState(""); // roomId cho chat
  const [userId, setUserId] = useState(null); // userId

  const token = localStorage.getItem("jwtToken");

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
          setFriends(response.data.data.friendList);
          setUserId(response.data.data.userId);
        } else {
          setFriendError(response.data.message || "Lỗi khi tải danh sách bạn bè.");
        }
      } catch (err) {
        setFriendError("Lỗi mạng hoặc server không khả dụng.");
      }
    };

    fetchFriends();
  }, []);

  // Fetch tin nhắn
  const fetchMessages = async (id) => {
    try {
      const response = await axios.post(
        `http://localhost:8080/api/v1/rooms/messages`,
        { receiverId: id },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      if (response.data.status === 200) {
        setChatError(null);
        setRoomId(response.data.data.roomId);
        setMessages(response.data.data.messages);
      } else {
        setChatError("Lỗi khi tải tin nhắn.");
        console.log("API response error: ", response.data.message); // Log lỗi nếu có
      }
    } catch (err) {
      setChatError("Lỗi mạng khi tải tin nhắn.");
      console.log("Network error:", err); // Log lỗi chi tiết từ axios
    }
  };

  // Xử lý khi click vào bạn bè
  const handleFriendClick = (friend) => {
    setSelectedFriend(friend);
  };

  useEffect(() => {
    if (selectedFriend) {
      fetchMessages(selectedFriend.id);
    }
  }, [selectedFriend]);

  // Kết nối WebSocket khi roomId thay đổi
  useEffect(() => {
    if (!roomId) return; // Không kết nối nếu chưa có roomId

    const socket = new SockJS("http://localhost:8080/chat");
    const client = new Client({
      webSocketFactory: () => socket,
      onConnect: () => {
        console.log("WebSocket kết nối thành công!");
        // Đăng ký nhận tin nhắn cho roomId
        client.subscribe(`/topic/${roomId}`, (message) => {
          const newMessage = JSON.parse(message.body);
          setMessages((prevMessages) => [...prevMessages, newMessage]);
        });
      },
      onStompError: (frame) => {
        console.error("Lỗi WebSocket:", frame);
      },
    });

    client.activate();
    setStompClient(client);

    // Dọn dẹp WebSocket khi component bị unmount hoặc roomId thay đổi
    return () => {
      if (client) {
        client.deactivate();
      }
    };
  }, [roomId, token]); // Kết nối lại WebSocket mỗi khi roomId thay đổi

  // Gửi tin nhắn qua WebSocket
  const handleSendMessage = () => {
    if (!newMessage.trim()) return; // Kiểm tra nếu tin nhắn trống
    if (!stompClient || !stompClient.connected) return; // Kiểm tra kết nối WebSocket

    //Tạo body gửi tin nhắn
    const messagePayload = {
      senderId: userId, // Thêm senderId vào tin nhắn
      content: newMessage, // Nội dung tin nhắn
    };

    // Gửi tin nhắn qua WebSocket
    stompClient.publish({
      destination: `/app/sendMessage/${roomId}`,
      body: JSON.stringify(messagePayload),
    });
    setNewMessage(""); // Xóa input sau khi gửi
  };

  return (
    <div className="chat-container">
      {/* Sidebar */}
      <div className="chat-sidebar">
        <h3>Danh sách bạn bè</h3>
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
              <p>Không có bạn bè.</p>
            )}
          </ul>
        )}
      </div>

      {/* Chat Section */}
      <div className="chat-main">
        {selectedFriend ? (
          <div className="chat-content">
            <h3>Chat với {selectedFriend.fullName}</h3>
            {chatError ? (
              <p className="error-message">{chatError}</p>
            ) : (
              <div className="messages">
                {messages.length > 0 ? (
                  messages.map((msg, index) => (
                    <div
                      key={index}
                      className={`message ${msg.senderId === userId ? "from-me" : "from-them"}`}
                    >
                      {msg.content}
                    </div>
                  ))
                ) : (
                  <p>Chưa có tin nhắn nào.</p>
                )}
              </div>
            )}
            <div className="chat-input">
              <input
                type="text"
                value={newMessage}
                onChange={(e) => setNewMessage(e.target.value)}
                placeholder="Gõ tin nhắn..."
              />
              <button onClick={handleSendMessage}>Gửi</button>
            </div>
          </div>
        ) : (
          <p>Chọn bạn để bắt đầu trò chuyện.</p>
        )}
      </div>
    </div>
  );
};

export default Chat;
