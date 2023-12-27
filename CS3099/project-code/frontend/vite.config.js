import { fileURLToPath, URL } from "node:url";

import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";

import dotenv from "dotenv";
dotenv.config();

// eslint-disable-next-line no-undef
const FPF_BACKEND_PORT = process.env.FPF_BACKEND_PORT || "8024";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },
  server: {
    // proxy for local machine running
    proxy: {
      "/api": {
        target: "http://localhost:" + FPF_BACKEND_PORT,
        rewrite: (path) => path.replace(/^\/api/, ""),
      },
    },
  },
});
