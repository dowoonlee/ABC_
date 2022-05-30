import Vue from "vue";
import Vuex from "vuex";
import axios from "axios";

Vue.use(Vuex);
const REST_API = `http://localhost:9999/api`;

export default new Vuex.Store({
  state: {},
  getters: {},
  mutations: {},
  actions: {
    getUserinfo(text, payload) {
      text;
      const API_URL = `${REST_API}/userinfo`;
      axios({
        url: API_URL,
        method: "POST",
        params: {
          region: payload.region,
          summonerName: payload.summonerName,
        },
      })
        .then((res) => {
          console.log(res.data);
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
  modules: {},
});
