 

import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios'

Vue.use(Vuex)


export default new Vuex.Store({
  state: {
    allStocks:[],
    allProducts:[],
  },
  mutations: {
    loadProducts(state, data) {
      state.allProducts = data
    },
    loadStocks(state, data) {
      state.allStocks = data
    },
  },
  actions: {
    loadProducts({ commit }) {
      axios.get('http://localhost:8080/api/products')
      .then(function (response) {
        commit('loadProducts', response.data)
      })
      .catch(function (error) {
        console.log(error);
      });
    },
    loadStocks({ commit }) {
      axios.get('http://localhost:8080/api/stocks')
      .then(function (response) {
        commit('loadStocks', response.data)
      })
      .catch(function (error) {
        console.log(error);
      });
    },
  },
  getters: {
         
  }
})
