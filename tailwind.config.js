/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./src/main/resources/**/*.{html,js,ts}"],
  safelist: [
    'border-red-400'
  ],
  theme: {
    extend: {},
  },
  variants: {
    display:['group-hover']
  },
  plugins: [],
}

