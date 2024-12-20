import React, { useState, useEffect } from "react";
import axios from "axios";
import { FaThumbsUp, FaCommentAlt, FaShare } from "react-icons/fa";
import "./Css/Home.css";
import { Link } from "react-router-dom";

const Home = () => {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [comments, setComments] = useState([]);
  const [activePostId, setActivePostId] = useState(null);
  const [showComments, setShowComments] = useState(false);
  const [newComment, setNewComment] = useState(""); // Bình luận mới
  const [isCommentNull, setIsCommentNull] = useState(false); // Thêm trạng thái lỗi

  useEffect(() => {
    const token = localStorage.getItem("jwtToken");

    const checkStatus = async (posts) => {
      const updatedPosts = await Promise.all(
        posts.map(async (post) => {
          let isLiked = false;
          let isShared = false;

          try {
            const likeResponse = await axios.get(
              `http://localhost:8080/api/v1/like/blog/${post.id}/isLiked`,
              { headers: { Authorization: `Bearer ${token}` } }
            );
            isLiked = likeResponse.status === 200 ? likeResponse.data.data : false;
          } catch (error) {
            console.error(`Error checking like status for post ${post.id}:`, error);
          }

          try {
            const shareResponse = await axios.get(
              `http://localhost:8080/api/v1/share/${post.id}/isShared`,
              { headers: { Authorization: `Bearer ${token}` } }
            );
            isShared = shareResponse.status === 200 ? shareResponse.data.data : false;
          } catch (error) {
            console.error(`Error checking share status for post ${post.id}:`, error);
          }

          return { ...post, isLiked, isShared };
        })
      );
      setPosts(updatedPosts);
    };

    axios
      .get("http://localhost:8080/api/v1/blog/all", {
        headers: {
          Authorization: `Bearer ${token}`,
          "Access-Control-Allow-Origin": "*",
        },
      })
      .then((response) => {
        if (response.status === 200) {
          const posts = response.data.data;
          checkStatus(posts);
        }
        setLoading(false);
      })
      .catch((error) => {
        setError("Hãy đăng nhập để xem các bài viết.");
        setLoading(false);
        console.error("Error:", error);
      });
  }, []);

  const handleLike = async (postId) => {
    const token = localStorage.getItem("jwtToken");

    try {
      const response = await axios.post(
        `http://localhost:8080/api/v1/like/blog/${postId}`,
        {},
        { headers: { Authorization: `Bearer ${token}` } }
      );

      if (response.status === 200) {
        setPosts((prevPosts) =>
          prevPosts.map((post) =>
            post.id === postId
              ? {
                  ...post,
                  likesCount:
                    response.data.data === "Like"
                      ? post.likesCount + 1
                      : post.likesCount - 1,
                  isLiked: response.data.data === "Like",
                }
              : post
          )
        );
      }
    } catch (error) {
      console.error("Error liking post:", error);
    }
  };

  const handleShare = async (postId, isShared) => {
    const token = localStorage.getItem("jwtToken");

    try {
      let response;
      if (isShared) {
        response = await axios.post(
          `http://localhost:8080/api/v1/share/${postId}/delete`,
          {},
          { headers: { Authorization: `Bearer ${token}` } }
        );
      } else {
        response = await axios.post(
          `http://localhost:8080/api/v1/share/${postId}`,
          { displayZone: "PUBLIC" },
          { headers: { Authorization: `Bearer ${token}` } }
        );
      }

      if (response.status === 200) {
        const isNowShared = !isShared;
        const sharesCountChange = isNowShared ? 1 : -1;

        setPosts((prevPosts) =>
          prevPosts.map((post) =>
            post.id === postId
              ? {
                  ...post,
                  isShared: isNowShared,
                  sharesCount: post.sharesCount + sharesCountChange,
                }
              : post
          )
        );
      }
    } catch (error) {
      console.error("Error handling share/unshare:", error);
    }
  };

  const fetchComments = async (postId) => {
    const token = localStorage.getItem("jwtToken");

    try {
      const response = await axios.get(
        `http://localhost:8080/api/v1/blog/${postId}/comment`,
        { headers: { Authorization: `Bearer ${token}` } }
      );

      if (response.status === 200) {
        setComments(response.data.data);
        setActivePostId(postId);
        setShowComments(true);
      }
    } catch (error) {
      console.error(`Error fetching comments for post ${postId}:`, error);
    }
  };

  const handleCommentSubmit = async (postId) => {
    const token = localStorage.getItem("jwtToken");
  
    try {
      if (!newComment.trim()) {
        setIsCommentNull(true); // Đánh dấu lỗi nếu bình luận trống
        return;
      }
      const response = await axios.post(
        `http://localhost:8080/api/v1/comment/${postId}/add`,
        { message: newComment },
        { headers: { Authorization: `Bearer ${token}` } }
      );
  
      if (response.status === 200) {
        setComments((prevComments) => [...prevComments, response.data.data]);
        setNewComment(""); // Xóa nội dung trong input sau khi gửi
  
        // Cập nhật số lượng bình luận trực tiếp cho bài viết
        setPosts((prevPosts) =>
          prevPosts.map((post) =>
            post.id === postId
              ? { ...post, commentsCount: post.commentsCount + 1 } // Cộng thêm 1 vào commentsCount
              : post
          )
        );
      }
    } catch (error) {
      console.error(`Error adding comment to post ${postId}:`, error);
    }
  };
  

  const toggleComments = (postId) => {
    if (activePostId === postId && showComments) {
      setShowComments(false);
      setActivePostId(null);
    } else {
      fetchComments(postId);
    }
  };

  // Xử lý khi mất focus (click ra ngoài)
  const handleBlur = () => {
    if (!newComment.trim()) {
      setIsCommentNull(false); // Loại bỏ viền đỏ khi mất focus và input trống
    }
  };

  if (loading) {
    return <div className="home-container">Đang tải dữ liệu...</div>;
  }

  if (error) {
    return <div className="home-container">{error}</div>;
  }

  return (
    <div className="home-container">
      <div className="posts">
        {posts.map((post) => (
          <div key={post.id} className="post">
            <h2 className="post-title">{post.title}</h2>
            <p className="post-content">{post.content}</p>
            <div className="post-details">
            <span className="author">
              Tác giả: <Link to={`/profile/${post.authorId}`}>{post.author}</Link>
            </span>
              <span
                className="likes"
                onClick={() => handleLike(post.id)}
                style={{
                  cursor: "pointer",
                  color: post.isLiked ? "blue" : "gray",
                }}
              >
                <FaThumbsUp /> {post.likesCount}
              </span>
              <span
                className="comments"
                onClick={() => toggleComments(post.id)}
                style={{
                  cursor: "pointer",
                  color:
                    activePostId === post.id && showComments ? "blue" : "gray",
                }}
              >
                <FaCommentAlt /> {post.commentsCount}
              </span>
              <span
                className="shares"
                onClick={() => handleShare(post.id, post.isShared)}
                style={{
                  cursor: "pointer",
                  color: post.isShared ? "green" : "gray",
                }}
              >
                <FaShare /> {post.sharesCount}
              </span>
              <span className="created-at">
                Ngày tạo: {new Date(post.createdAt).toLocaleDateString()}
              </span>
            </div>
            {activePostId === post.id && showComments && (
              <div className="comments-section">
                <div className="comments-list">
                  {comments.length > 0 ? (
                    comments.map((comment) => (
                      <div key={comment.commentId} className="comment">
                        <p className="comment-content">{comment.content}</p>
                        <span className="comment-author">
                          <Link to={`/profile/${comment.userId}`}>{comment.fullName}</Link>
                        </span>
                        <span className="comment-time">
                          {new Date(comment.createdAt).toLocaleString()}
                        </span>
                      </div>
                    ))
                  ) : (
                    <p>Chưa có bình luận nào.</p>
                  )}
                </div>
                <div className="comment-input">
                  <input
                    type="text"
                    value={newComment}
                    onChange={(e) => setNewComment(e.target.value)}
                    placeholder="Viết bình luận..."
                    className={isCommentNull ? "input-error" : ""}
                    onBlur={handleBlur} // Loại bỏ viền đỏ khi mất focus
                  />
                  <button onClick={() => handleCommentSubmit(post.id)}>
                    Gửi
                  </button>
                </div>
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

export default Home;
