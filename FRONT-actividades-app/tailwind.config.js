/** @type {import('tailwindcss').Config} */
module.exports = {
  darkMode: 'class',
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {
      colors: {
        'baldwin': {
          // Identidad: El alma del uniforme
          'verde': '#2A6F4F',   
          // Interacción: Para botones y elementos clicables
          'cobalto': '#2B58C1', 
          // Resaltado: Para estados, avisos o detalles de lujo
          'oro': '#C99738',     
          // Neutros: Para que la interfaz respire
          'dark': '#1E293B',    // Slate 800 (Títulos)
          'surface': '#F8FAFC', // Slate 50 (Fondo App)
          'border': '#E2E8F0',  // Slate 200 (Divisores)
        }
      },
      fontFamily: {
        // Inter es la estándar para SaaS por su alta legibilidad
        sans: ['Inter', 'ui-sans-serif', 'system-ui', 'sans-serif'],
      },
    },
  },
  plugins: [],
}