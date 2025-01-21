"use strict";
const divSideImages = document.getElementById('divSideImages');
const children = divSideImages === null || divSideImages === void 0 ? void 0 : divSideImages.children;
if (children) {
    Array.from(children).forEach((child) => {
        child.addEventListener('click', (event) => {
            setActiveSideImage(child);
        });
    });
}
let currActiveSideImg;
const imgMain = document.getElementById('imgMain');
const activeImages = document.getElementsByClassName('imgActive');
if (activeImages.length > 0)
    currActiveSideImg = activeImages[0];
function setActiveSideImage(img) {
    if (currActiveSideImg)
        currActiveSideImg.className = '';
    img.className = 'imgActive';
    currActiveSideImg = img;
    updateMainImage();
}
function updateMainImage() {
    if (imgMain && currActiveSideImg)
        imgMain.src = currActiveSideImg.src;
}
