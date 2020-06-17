<template>
  <div id="app">
    <v-app >
      <v-toolbar :clipped-left="$vuetify.breakpoint.mdAndUp" app fixed class="nav_background_color">

        <router-link to="/"></router-link>
        <!-- <v-spacer></v-spacer> -->
        <!-- <v-text-field light="" outline append-icon="search" clearable hide-details single-line v-model="searchInput" placeholder="Search by category or product name" class="searchbox"></v-text-field> -->
        <!-- <v-spacer></v-spacer> -->
        <v-progress-circular indeterminate v-if="load"  color="primary" ></v-progress-circular>
        <!-- <v-btn round :to="`/fileUpload`" ><v-icon class="mr-2" large>cloud_upload</v-icon> Upload CSV</v-btn> -->
        <!-- <v-btn round :to="`/info`" ><v-icon large>list</v-icon>Products</v-btn> -->
        <v-btn round :to="`/productEdit`"  ><v-icon large>list</v-icon>Products</v-btn>
        <v-btn round :to="`/stockEdit`" ><v-icon large>list</v-icon>Stocks</v-btn>
        <v-btn round :to="`/about`" ><v-icon  large>info</v-icon> About</v-btn>

      </v-toolbar>

      <main>
        <router-view :key="$route.fullPath"></router-view>
      </main>
    </v-app>
    
  </div>
</template>

<script>
import axios from "axios"
export default {
  name: 'app',
  data () {
    return {
      load:false,
      searchInput:null,
    }
  },
  watch: {

  },
  created() {
    this.$router.push({ path: `/about` })
    this.$store.dispatch('loadProducts')
    this.$store.dispatch('loadStocks')
  },
  methods:{
    
  },
  computed:{
    user(){
      return this.$store.state.user 
    },
  }
}
</script>

<style>
#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 75px;
}
.v-btn:not(.v-btn--depressed):not(.v-btn--flat) {
  box-shadow: none;
}
h1, h2 {
  font-weight: normal;
}

</style>
