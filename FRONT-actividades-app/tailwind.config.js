/** @type {import('tailwindcss').Config} */
module.exports = {
  darkMode: 'class',
  content: ["./src/**/*.{html,ts}"],
  theme: {
    extend: {
      colors: {
        'baldwin': {
          'verde': '#2A6F4F',
          'cobalto': '#2B58C1',
          'oro': '#C99738',
          'surface': '#F8FAFC',
          'slate-txt': '#475569',
        }
      },
      backgroundImage: {
        // El gradiente azul-verde que pediste para botones principales
        'btn-gradient': 'linear-gradient(135deg, #2B58C1 0%, #2A6F4F 100%)',
      },
      fontFamily: {
        'sans': ['Inter', 'ui-sans-serif', 'system-ui'],
        'display': ['Merriweather Sans', 'sans-serif'], // Para títulos y branding
      },
    },
  },
}