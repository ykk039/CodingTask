 

import Vue from 'vue'
import Router from 'vue-router'

import FileUpload from '../components/FileUpload'
import Home from '../components/Home'
import ProductEdit from '../components/ProductEdit'
import StockEdit from '../components/StockEdit'
import About from '../components/About'


Vue.use(Router)

export default new Router({
  // mode: 'history',
  routes: [

    {
      path: '/',
      name: 'Home',
      component: Home,
    },
    {
      path: '/productEdit',
      name: 'productEdit',
      component: ProductEdit,
    },
    {
      path: '/stockEdit',
      name: 'stockEdit',
      component: StockEdit,
    },
    {
      path: '/fileUpload',
      name: 'FileUpload',
      component: FileUpload,
    },
    {
      path: '/about',
      name: 'About',
      component: About,
    },

  
  ],

})
