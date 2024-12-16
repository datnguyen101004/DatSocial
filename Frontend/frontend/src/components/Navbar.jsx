import React from "react";
import "./Css/Navbar.css"; // Import CSS
import { Link } from "react-router-dom";

const Navbar = () => {
  return (
    <nav className="navbar">
      <Link to="/" className="navbar-brand">DatSocial</Link>
      <ul className="navbar-menu">
        <li><Link to="/login">login</Link></li>
        <li><Link to="/register">signup</Link></li> 
        <li>
          <input type="text" className="search-bar" placeholder="Search..." />
        </li>
      </ul>
    </nav>
  );
};

export default Navbar;
