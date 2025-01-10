const divSideImages = document.getElementById('divSideImages');
const children = divSideImages?.children;

if (children) {
    Array.from(children).forEach((child) => {
        child.addEventListener('click', (event) => {
            setActiveSideImage(child as HTMLImageElement);
        })
    })
}

let currActiveSideImg: HTMLImageElement | null;

const imgMain: HTMLImageElement = document.getElementById('imgMain') as HTMLImageElement;
const activeImages = document.getElementsByClassName('imgActive');
if (activeImages.length > 0) currActiveSideImg = activeImages[0] as HTMLImageElement;

function setActiveSideImage(img: HTMLImageElement) {
    if (currActiveSideImg) currActiveSideImg.className = '';
    img.className = 'imgActive';
    currActiveSideImg = img;
    updateMainImage();
}

function updateMainImage() {
    if (imgMain && currActiveSideImg) imgMain.src = currActiveSideImg.src;
}