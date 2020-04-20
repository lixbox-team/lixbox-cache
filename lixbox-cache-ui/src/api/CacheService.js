import axios from "axios";

export class CacheService {
  API_URL = "";
  DEFAULT_REQUEST_CONF = {
    headers: {
      "Content-Type": "application/json",
    },
  };

  constructor(url) {
    this.API_URL = url;
  }

  getKeys() {
    const url = `${this.API_URL}/keys/`;
    return axios.get(url).then((response) => response.data);
  }

  put(key, value) {
    const url = this.API_URL + "/value/" + key;
    return axios
      .post(url, value, this.DEFAULT_REQUEST_CONF)
      .then((response) => response.data);
  }

  get(key) {
    const url = this.API_URL + "/value/" + key;
    return axios.get(url).then((response) => response.data);
  }

  remove(key) {
    const url = this.API_URL + "/keys/" + key;
    return axios.delete(url).then((response) => response.data);
  }
}
