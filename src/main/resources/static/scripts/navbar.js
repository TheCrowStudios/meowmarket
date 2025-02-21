"use strict";
function showUserDropdown() {
    const userDropdown = document.getElementById('user-dropdown');
    userDropdown === null || userDropdown === void 0 ? void 0 : userDropdown.classList.remove;
}
function toggleNavbar() {
    const sideNav = document.getElementById('side-nav');
    const sideNavLinks = document.getElementById('side-nav-links');
    sideNavLinks === null || sideNavLinks === void 0 ? void 0 : sideNavLinks.classList.toggle('-translate-x-full');
    sideNavLinks === null || sideNavLinks === void 0 ? void 0 : sideNavLinks.classList.toggle('opacity-0');
}
