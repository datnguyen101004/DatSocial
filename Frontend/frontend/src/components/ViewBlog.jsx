import React, { useEffect, useState } from "react";
import axios from "axios";
import { FaThumbsUp, FaCommentAlt, FaShare } from "react-icons/fa";
import { Link } from "react-router-dom";
import "./Css/ViewBlog.css";

const ViewBlog = ({ post, setPosts }) => {
  const [comments, setComments] = useState([]);
  const [activePostId, setActivePostId] = useState(null);
  const [showComments, setShowComments] = useState(false);
  const [newComment, setNewComment] = useState("");
  const [isCommentNull, setIsCommentNull] = useState(false);

  const token = localStorage.getItem("jwtToken");

  const handleLike = async (postId) => {
    try {
      const response = await axios.post(
        `http://localhost:8080/api/v1/like/blog/${postId}`,
        {},
        { headers: { Authorization: `Bearer ${token}` } }
      );

      if (response.status === 200) {
        setPosts((prevPosts) =>
          prevPosts.map((p) =>
            p.id === postId
              ? {
                  ...p,
                  likesCount:
                    response.data.data === "Like"
                      ? p.likesCount + 1
                      : p.likesCount - 1,
                  isLiked: response.data.data === "Like",
                }
              : p
          )
        );
      }
    } catch (error) {
      console.error("Error liking post:", error);
    }
  };

  const handleShare = async (postId, isShared) => {
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
          prevPosts.map((p) =>
            p.id === postId
              ? {
                  ...p,
                  isShared: isNowShared,
                  sharesCount: p.sharesCount + sharesCountChange,
                }
              : p
          )
        );
      }
    } catch (error) {
      console.error("Error handling share/unshare:", error);
    }
  };

  const fetchComments = async (postId) => {
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
    if (!newComment.trim()) {
      setIsCommentNull(true);
      return;
    }

    try {
      const response = await axios.post(
        `http://localhost:8080/api/v1/comment/${postId}/add`,
        { message: newComment },
        { headers: { Authorization: `Bearer ${token}` } }
      );

      if (response.status === 200) {
        setComments((prevComments) => [...prevComments, response.data.data]);
        setNewComment("");

        setPosts((prevPosts) =>
          prevPosts.map((p) =>
            p.id === postId
              ? { ...p, commentsCount: p.commentsCount + 1 }
              : p
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

  return (
    <div className="post">
      <h2 className="post-title">{post.title}</h2>
      <p className="post-content">{post.content}</p>
      <div className="post-details">
        <span className="author">
          Tác giả: <Link to={`/profile/${post.authorId}`}>{post.author}</Link>
        </span>
        <span
          className="likes"
          onClick={() => handleLike(post.id)}
          style={{ cursor: "pointer", color: post.isLiked ? "blue" : "gray" }}
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
          style={{ cursor: "pointer", color: post.isShared ? "green" : "gray" }}
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
                    <Link to={`/profile/${comment.userId}`}>
                      {comment.fullName}
                    </Link>
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
              onBlur={() => setIsCommentNull(false)}
            />
            <button onClick={() => handleCommentSubmit(post.id)}>Gửi</button>
          </div>
        </div>
      )}
    </div>
  );
};

export default ViewBlog;
