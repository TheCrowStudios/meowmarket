"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
document.addEventListener("DOMContentLoaded", () => {
    const gridItems = document.querySelectorAll('.grid-animated > div');
    const isInViewport = (element) => {
        const rect = element.getBoundingClientRect();
        return (rect.left >= 0 && rect.bottom <= ((window.innerHeight || document.documentElement.clientHeight) + element.clientHeight * 0.5) && rect.right <= (window.innerWidth || document.documentElement.clientWidth));
    };
    const animateItems = () => __awaiter(void 0, void 0, void 0, function* () {
        for (let i = 0; i < gridItems.length; i++) {
            const index = i;
            const item = gridItems[i];
            if (index === 0) {
                const rect = item.getBoundingClientRect();
                console.log(`top: ${rect.top} bottom: ${rect.bottom} formula: ${(window.innerHeight || document.documentElement.clientHeight) + item.clientHeight * 0.5}`);
            }
            if (isInViewport(item) && !item.classList.contains('animated')) {
                if (index % 2 === 0) {
                    item.classList.add('slide-from-left');
                }
                else {
                    item.classList.add('slide-from-right');
                }
                item.classList.add('animated');
            }
            if (index % 2 === 1) {
                yield new Promise(r => setTimeout(r, 200));
            }
        }
    });
    animateItems();
    window.addEventListener('scroll', () => {
        animateItems();
    });
    const slideshow = document.getElementById('slideshow');
    const slider = document.getElementById('slideshow-container');
    const dotsDiv = document.getElementById('dots');
    let currentIndex = 0;
    let resetTimer = false;
    const slides = Array.from(document.querySelectorAll('#slideshow-container > div'));
    const totalSlides = slides.length;
    for (let i = 0; i < totalSlides; i++) {
        const dot = document.createElement('div');
        dot.classList.add('w-3', 'h-3', 'rounded-full', 'bg-white', 'opacity-50', 'cursor-pointer');
        dot.addEventListener('click', () => forceSlide(i));
        dotsDiv === null || dotsDiv === void 0 ? void 0 : dotsDiv.appendChild(dot);
    }
    const dots = dotsDiv.querySelectorAll('div');
    function forceSlide(index) {
        resetTimer = true;
        goToSlide(index);
    }
    function goToSlide(index) {
        currentIndex = index;
        slides.forEach((slide, index) => {
            if (index === currentIndex) {
                slide.style.transform = 'translateX(0)';
                slide.style.opacity = '1';
                slide.style.zIndex = '10';
            }
            else {
                slide.style.transform = 'translateX(-100%)';
                slide.style.opacity = '0';
                slide.style.zIndex = '0';
                setTimeout(() => { slide.style.transform = 'translateX(100%)'; }, 500);
            }
        });
        dots.forEach((dot, i) => {
            if (i === index) {
                dot.classList.remove('opacity-50');
                dot.classList.add('opacity-100');
            }
            else {
                dot.classList.add('opacity-50');
                dot.classList.remove('opacity-100');
            }
        });
    }
    function nextSlide() {
        if (resetTimer) {
            resetTimer = false;
            return;
        }
        currentIndex = (currentIndex + 1) % totalSlides;
        goToSlide(currentIndex);
    }
    function init() {
        slides.forEach((slide, i) => {
            if (i !== 0) {
                slide.style.opacity = '0';
                slide.style.zIndex = '0';
            }
        });
        goToSlide(0);
    }
    init();
    setInterval(nextSlide, 5000);
});
