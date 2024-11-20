import axios from "axios";

const instance = axios.create({
  baseURL: "http://localhost:8080",
  timeout: 10000,
  headers: {
    "Content-Type": "application/json",
    // 'Authorization': 'Bearer yourToken' // Optional header for authorization
  },
});

export default instance;
