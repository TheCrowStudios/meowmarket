"use strict";
function showUserDropdown() {
    const userDropdown = document.getElementById('user-dropdown');
    userDropdown === null || userDropdown === void 0 ? void 0 : userDropdown.classList.remove;
}
function toggleNavbar() {
    const sideNav = document.getElementById('side-nav');
    const sideNavLinks = document.getElementById('side-nav-links');
    sideNav === null || sideNav === void 0 ? void 0 : sideNav.classList.toggle('w-16');
    sideNav === null || sideNav === void 0 ? void 0 : sideNav.classList.toggle('w-screen');
    sideNav === null || sideNav === void 0 ? void 0 : sideNav.classList.toggle('h-screen');
    sideNav === null || sideNav === void 0 ? void 0 : sideNav.classList.toggle('bg-primary');
    sideNavLinks === null || sideNavLinks === void 0 ? void 0 : sideNavLinks.classList.toggle('w-0');
    sideNavLinks === null || sideNavLinks === void 0 ? void 0 : sideNavLinks.classList.toggle('w-screen');
    sideNavLinks === null || sideNavLinks === void 0 ? void 0 : sideNavLinks.classList.toggle('flex');
    sideNavLinks === null || sideNavLinks === void 0 ? void 0 : sideNavLinks.classList.toggle('hidden');
}
