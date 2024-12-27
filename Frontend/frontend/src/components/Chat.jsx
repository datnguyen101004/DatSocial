import React, { useEffect, useState } from "react";
import axios from "axios";
import "./Css/Chat.css";
import { useParams } from "react-router-dom";

const Chat = () => {
  const [friends, setFriends] = useState([]); // Danh sách bạn bè
  const [friendError, setFriendError] = useState(""); // Lỗi bạn bè
  const [chatError, setChatError] = useState(""); // Lỗi chat
  const [selectedFriend, setSelectedFriend] = useState(null); // Bạn bè đã chọn
  const [messages, setMessages] = useState([]); // Tin nhắn
  const [newMessage, setNewMessage] = useState(""); // Tin nhắn mới
  const token = localStorage.getItem("jwtToken");
  const { friendId } = useParams();

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
          if (friendId) {
            const selected = response.data.data.find((friend) => friend.id === parseInt(friendId));
            setSelectedFriend(selected);
            fetchMessages(selected.id); // Lấy tin nhắn nếu đã có friendId
          }
        } else {
          setFriendError(response.data.message || "Error fetching friends.");
        }
      } catch (err) {
        setFriendError("Network error or server unavailable.");
      }
    };

    fetchFriends();
  }, [friendId, token]);

  // Fetch tin nhắn
  const fetchMessages = async (id) => {
    try {
      const response = await axios.get(`http://localhost:8080/api/v1/chat/${id}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (response.data.status === 200) {
        setMessages(response.data.data);
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

  // Gửi tin nhắn
  const handleSendMessage = async () => {
    if (!newMessage.trim()) return;
    try {
      const response = await axios.post(
        `http://localhost:8080/api/v1/chat/${selectedFriend.id}`,
        { message: newMessage },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      if (response.data.status === 200) {
        setMessages([...messages, { text: newMessage, fromMe: true }]);
        setNewMessage("");
      } else {
        setChatError("Error sending message.");
      }
    } catch (err) {
      setChatError("Network error while sending message.");
    }
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
                    <div
                      key={index}
                      className={`message ${msg.fromMe ? "from-me" : "from-them"}`}
                    >
                      {msg.text}
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
