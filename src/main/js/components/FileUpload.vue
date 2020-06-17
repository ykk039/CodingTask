
<template>
  <div class="container">
    <v-card>
      <div class="large-12 medium-12 small-12 cell">

        <form @submit.prevent="sendFile()" enctype="multipart/form-data" >
          <div class="field">
            <p>Product</p>
          </div>
          <div class="field">
            <label for ="file" class="label">Upload Files</label>
            <input name="csvfile" type="file" ref="file" @change="handleFileUpload()"/>
          </div>
          <div class="field">
            <button class="button is-info">Send</button>
          </div>
        </form>

      </div>
    </v-card>
    <v-container>
    </v-container>
    <v-card>
      <div class="large-12 medium-12 small-12 cell">

        <form @submit.prevent="sendStockFile()" enctype="multipart/form-data" >
          <div class="field">
            <p>Stocks</p>
          </div>
          <div class="field">
            <label for ="file" class="label">Upload Files</label>
            <input name="csvfile" type="file" ref="file" @change="handleStockFileUpload()"/>
          </div>
          <div class="field">
            <button class="button is-info">Send</button>
          </div>
        </form>

      </div>
    </v-card>
  </div>
</template>

<script>
import axios from 'axios'
  export default {
    data(){
      return {
        file: '',
        stockFile:''
      }
    },

    methods: {

    handleFileUpload(){
      this.file = this.$refs.file.files[0];
    },
    handleStockFileUpload(){
      this.stockFile = this.$refs.file.files[0];
    },
    sendFile(){
      const formData = new FormData();
      formData.append('file', this.file);

      try{
        var headers =  {"Content-type": "application/csv"}
         axios.post( 'http://localhost:8080/api/products/upload', formData, headers );
      }catch(err){
         console.log(err)
      }finally{
        alert("uploaded")
        setTimeout(function(){ this.$store.dispatch('loadProducts') }.bind(this), 2000);
      }
      
    },
    sendStockFile(){
      const formData = new FormData();
      formData.append('file', this.stockFile);

      try{
        var headers =  {"Content-type": "application/csv"}
         axios.post( 'http://localhost:8080/api/stocks/upload', formData, headers );
      }catch(err){
         console.log(err)
      }finally{
        alert("uploaded")
        setTimeout(function(){ this.$store.dispatch('loadStocks') }.bind(this), 2000);
      }
      
    },
  }
}
</script>

<style scoped>
input{
  border-style:ridge
}
</style>