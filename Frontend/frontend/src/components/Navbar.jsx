import React from "react";
import "./Css/Navbar.css"; // Import CSS
import { Link } from "react-router-dom";

const Navbar = () => {
  const jwtToken = localStorage.getItem("jwtToken"); // Kiểm tra token trong localStorage

  return (
    <nav className="navbar">
      <Link to="/" className="navbar-brand">DatSocial</Link>
      <ul className="navbar-menu">
        {jwtToken ? (
          // Nếu có jwtToken -> Hiển thị search bar
          <>
          <li>
            <input type="text" className="search-bar" placeholder="Search..." />
          </li>
          <li><Link to="/addBlog">New Blog</Link></li>
          <li><Link to="/profile">My Profile</Link></li>
          </>
        ) : (
          // Nếu không có jwtToken -> Hiển thị Login và Register
          <>
            <li><Link to="/login">Login</Link></li>
            <li><Link to="/register">Signup</Link></li>
          </>
        )}
      </ul>
    </nav>
  );
};

export default Navbar;