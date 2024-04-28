import axios from "axios";
axios.defaults.withCredentials = true;
const myAxios = axios.create({
    baseURL: 'http://localhost:8080/api'
});

export default myAxios