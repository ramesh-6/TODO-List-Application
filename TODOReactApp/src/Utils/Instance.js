import axios from "axios";

const instance = axios.create({
    baseURL: "http://localhost:8080",
    timeout: 10000,
    headers: {
        "Content-Type": "application/json"
    }
})

instance.interceptors.request.use(
    config => {
        const token = sessionStorage.getItem('jwtToken');
        const excludedEndpoints = ["/login", "/register"];
        
        if (token && !excludedEndpoints.some(endpoint => config.url.includes(endpoint))) {
            config.headers['Authorization'] = `Bearer ${token}`;
        }
        
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

export default instance;