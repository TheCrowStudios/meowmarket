document.addEventListener("DOMContentLoaded", () => {
    const gridItems = document.querySelectorAll('.grid-animated > div');
    const categoriesText = document.querySelector('#span-categories');

    const isInViewport = (element: HTMLElement) => {
        const rect = element.getBoundingClientRect();
        return (rect.left >= 0 && rect.bottom <= ((window.innerHeight || document.documentElement.clientHeight) + element.clientHeight * 0.6) && rect.right <= (window.innerWidth || document.documentElement.clientWidth));
    };

    const animateItems = async () => {
        for (let i = 0; i < gridItems.length; i++) {
            const index = i;
            const item = gridItems[i];

            if (index === 0) {
                const rect = item.getBoundingClientRect();
                // console.log(`top: ${rect.top} bottom: ${rect.bottom} formula: ${(window.innerHeight || document.documentElement.clientHeight) + item.clientHeight * 0.6}`)
            }

            if (isInViewport(item as HTMLElement) && !item.classList.contains('animated')) {
                if (!categoriesText?.classList.contains('animated')) {
                    categoriesText?.classList.remove('opacity-0');
                }

                if (index % 2 === 0) {
                    item.classList.add('slide-from-left');
                } else {
                    item.classList.add('slide-from-right');
                }

                item.classList.add('animated');
            }

            if (index % 2 === 1) {
                await new Promise(r => setTimeout(r, 200));
            }
        }
    };

    animateItems();

    window.addEventListener('scroll', () => {
        animateItems();
    });

    const slideshow = document.getElementById('slideshow') as HTMLDivElement;
    const slider = document.getElementById('slideshow-container') as HTMLDivElement;
    const dotsDiv = document.getElementById('dots') as HTMLDivElement;
    let currentIndex = 0;
    let resetTimer = false;
    const slides = Array.from(document.querySelectorAll('#slideshow-container > div')) as HTMLElement[];
    const totalSlides = slides.length;

    for (let i = 0; i < totalSlides; i++) {
        const dot = document.createElement('div') as HTMLDivElement;
        dot.classList.add('w-3', 'h-3', 'rounded-full', 'bg-white', 'opacity-50', 'cursor-pointer');
        dot.addEventListener('click', () => forceSlide(i));
        dotsDiv?.appendChild(dot);
    }

    const dots = dotsDiv.querySelectorAll('div');

    function forceSlide(index: number) {
        resetTimer = true;
        goToSlide(index);
    }

    function goToSlide(index: number) {
        currentIndex = index;

        slides.forEach((slide, index) => {
            if (index === currentIndex) {
                slide.style.transform = 'translateX(0)';
                slide.style.opacity = '1';
                slide.style.zIndex = '10';
            } else {
                slide.style.transform = 'translateX(-100%)';
                slide.style.opacity = '0';
                slide.style.zIndex = '0';
                setTimeout(() => { slide.style.transform = 'translateX(100%)' }, 500);
            }
        })

        dots.forEach((dot, i) => {
            if (i === index) {
                dot.classList.remove('opacity-50');
                dot.classList.add('opacity-100');
            } else {
                dot.classList.add('opacity-50');
                dot.classList.remove('opacity-100');
            }
        })
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
        })

        goToSlide(0);
    }

    init();

    setInterval(nextSlide, 5000);
})