<template>
<div class="left">

  <v-card class="ma-5">
    <div class="pl-5 pt-5">
   <div class="headline">About This Project</div><br>
   <p>User can add, edit, delete product one by one or by uploading CSV</p>
   <p>User can also add, edit, delete stock data one by one or by uploading CSV</p>
   <p>Transfering product between stocks are also fine</p>
<br>
<h2>Technologies and frameworks/libraries in use</h2>
<p>This Web App is a Single Page Application which is developed with Vuejs as the front-end framework and Java Spring-Boot as the back-end framework. The database we use is mySQL. </p>
</div>
      <v-container fluid grid-list-md>
            <v-layout row wrap>
              <v-flex xs6 > 
                <v-card>
                  <v-card-title class="subheading font-weight-bold">Advatanges</v-card-title>
                  <v-card-text>1.Small size comparing to Angular</v-card-text>
                  <v-card-text>2.Flexibility, similar to React, Angular and JS framework</v-card-text>
                </v-card>
              </v-flex>
              <v-flex xs6 > 
                <v-card>
                  <v-card-title class="subheading font-weight-bold">Disadvantages</v-card-title>
                  <v-card-text>1.Not as popular as Angular, hence, fewer recourses</v-card-text>
                  <v-card-text>2.Too Flexible</v-card-text>
                </v-card>
              </v-flex>
            </v-layout>
      </v-container>
  </v-card>
</div>
</template>
<script>


export default {
  name: "About",

  data() {
    return {

    }
  },

  methods: {

  },

};
</script>

<style scoped>
h1, h2 {
  font-weight: normal;
}
.left{
  text-align: left!important;
  
}
</style>