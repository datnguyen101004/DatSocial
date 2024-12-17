import React from "react";
import "./Css/Navbar.css"; // Import CSS
import { Link } from "react-router-dom";

const Navbar = () => {

  return (
    <nav className="navbar">
      <Link to="/home" className="navbar-brand">DatSocial</Link>
      <ul className="navbar-menu">
          <li>
            <input type="text" className="search-bar" placeholder="Search..." />
          </li>
          <li><Link to="/addBlog">New Blog</Link></li>
          <li><Link to="/profile">My Profile</Link></li>
      </ul>
    </nav>
  );
};

export default Navbar;