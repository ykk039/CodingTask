
<template>
  <div class="ma-5">
    <v-toolbar flat color="white">
      <v-toolbar-title>Products</v-toolbar-title>
    </v-toolbar>
    <v-toolbar flat color="white">
      <!-- <v-btn round  color="info" @click="back()"><v-icon small>arrow_back_ios</v-icon>Back</v-btn> -->
      <!-- <v-divider class="mx-2" inset vertical ></v-divider> -->
      <v-spacer></v-spacer>
      <v-text-field v-model="search" append-icon="search" label="Search" single-line hide-details></v-text-field>
      <v-select :items="fields" class="mx-5 mt-4" label="search by field" v-model="selected" ></v-select>
      <v-dialog v-model="dialog" max-width="500px">
        <v-btn slot="activator" color="info" dark class="mb-2">New Product</v-btn>
        <v-card v-if="uploadCSV == false">
          <v-card-title>
            <span class="headline">{{ formTitle }}</span>
          </v-card-title>
          <v-card-text>
            <v-container grid-list-md>
              <v-layout wrap>
                <v-flex xs12 sm6 md4>
                  <v-text-field v-model="editedItem.code" label="Product Code"></v-text-field>
                </v-flex>
                <v-flex xs12 sm6 md4>
                  <v-text-field v-model="editedItem.name" label="Product Name"></v-text-field>
                </v-flex>
                <v-flex xs12 sm6 md4>
                  <v-text-field v-model="editedItem.weight" label="Weight"></v-text-field>
                </v-flex>
              </v-layout>
            </v-container>
          </v-card-text>

          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" flat @click="close">Cancel</v-btn>
            <v-btn color="blue darken-1" flat @click="save">Save</v-btn>
          </v-card-actions>
        </v-card>

        <v-card v-if="uploadCSV ==true">
          <v-card-title>
            <span class="headline">Upload CSV</span>
          </v-card-title>
          <v-container grid-list-md>

            <form @submit.prevent="sendFile()" enctype="multipart/form-data" >
              <div class="field">
              </div>
              <div class="field">
                <label for ="file" class="label">Upload Files</label>
                <input name="csvfile" type="file" ref="file" @change="handleFileUpload()"/>
              </div>
              <div class="field">
                <button class="button is-info">Send</button>
              </div>
            </form>
          </v-container>
        </v-card>
      </v-dialog>
      <v-btn color="info" dark class="mb-2" @click="toUploadCSV" >Upload CSV</v-btn>
      <v-btn color="info" dark class="mb-2" @click="download" >Download as CSV</v-btn>
    </v-toolbar>
    <v-data-table :headers="headers" :items="products" :search="search" :custom-filter="customFilter" class="elevation-1" >
      <template slot="items" slot-scope="props">

          <td class="text-xs-left">{{ props.item.code }}</td>
          <td class="text-xs-left">{{ props.item.name }}</td>
          <td class="text-xs-left">{{ props.item.weight }}</td>
        <td class="justify-center layout px-0">
          <v-icon  small  class="mr-2" @click="editItem(props.item)" > edit </v-icon>
          <v-icon  small  @click="deleteItem(props.item)" >  delete  </v-icon>
        </td>


      </template>
      <v-alert slot="no-results" :value="true" color="error" icon="warning">
        Your search for "{{ search }}" found no results.
      </v-alert>
    </v-data-table>
     <v-alert v-if="alert" type="success"  > Success! </v-alert> 
  </div>
</template>

<script>
import axios from 'axios'
  export default {

    data: () => ({
      alert: false,
      dialog: false,
      search:'',
      selected:'',
      uploadCSV:false,
      fields: ['all','code', 'name','weight'],
          
      headers: [
          { text: 'Product Code ',align: 'left',value: 'code'},
          { text: 'Product Name ',align: 'left',value: 'name'},
          { text: 'Weight', value: 'weight' },
          { text: 'Action', align: 'left', sortable:false },
        ],
      editedIndex: -1,
      editedItem: {
        code:'', name: '', weight: '',
      },
      defaultItem: {
        code:'', name: '', weight: '',
      }
    }),
    

    watch: {
      dialog (val) {
        val || this.close()
      }
    },

    created () {

    },

    methods: {
      back(){
        this.$router.go(-1)
      },
      customFilter(items, search, filter) {
        var selected = this.selected
        search = search.toString().toLowerCase()
        if(selected=="all"){
           return items.filter(item => filter(item["code"], search) || filter(item["name"], search) || filter(item["weight"], search));
        }
        else if(selected) {
           return items.filter(row => filter(row[selected], search));
        }
        return items.filter(item => filter(item["code"], search) || filter(item["name"], search) || filter(item["weight"], search));

      },
      editItem (item) {
        console.log('check4');
        console.log(item);
        console.log(item.code);
        this.editedIndex = this.products.indexOf(item)
        this.editedItem = Object.assign({}, item)
        // this.editedItem.productCode = this.editedItem.code
        this.dialog = true
      },

      deleteItem (item) {
        const index = this.products.indexOf(item)
        var url = "http://localhost:8080/api/products/"+item.code
        var item_info = "Product Code: "+item.code+"\n"+ "Product Name: "+item.name+"\n"+  "Weight: "+item.weight+"\n"
        if(confirm('Are you sure you want to delete the following item?\n'+item_info)){
          axios.delete(url)
            .then(function (response) {
              alert("Deleted")
              this.$store.dispatch('loadProducts')
            }.bind(this))
            .catch(function (error) {
              alert(error);
            });
       } 
      },

      toUploadCSV(){
        this.uploadCSV = true
        this.dialog = true
      },

      download(){
        axios({
          url: 'http://localhost:8080/api/products/download',
          method: 'GET',
          responseType: 'blob',
        }).then((response) => {
          const url = window.URL.createObjectURL(new Blob([response.data]));
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', 'products.csv');
          document.body.appendChild(link);
          link.click();
        });
      },

      close () {
        this.dialog = false
        setTimeout(() => {
          this.editedItem = Object.assign({}, this.defaultItem)
          this.editedIndex = -1
          this.uploadCSV = false
        }, 300)
      },

      save () {
        if (this.editedIndex > -1) {
          console.log('check1')
          console.log(this.editedItem)
          console.log(this.editedItem.code)
          axios.put('http://localhost:8080/api/products/'+this.editedItem.code, this.editedItem)
            .then(function (response) {
              alert("Changed")
              this.$store.dispatch('loadProducts')
            }.bind(this))
            .catch(function (error) {
              console.log(error);
            });
        } else {
          console.log('check2')
          console.log(this.editedItem)
          axios.post('http://localhost:8080/api/products/', this.editedItem)
            .then(function (response) {
              alert("Created")
              this.$store.dispatch('loadProducts')

            }.bind(this))
            .catch(function (error) {
              console.log(error);
            });
        }
        this.close()
      },
      handleFileUpload(){
        this.file = this.$refs.file.files[0];
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
        this.close()
      },
    },
    
    computed: {
      formTitle () {
        return this.editedIndex === -1 ? 'New Product' : 'Edit Product'
      },
      products(){
        console.log('hihi');
        console.log( this.$store.state.allProducts);
        return this.$store.state.allProducts
      }
    },
  }
</script>
<style scoped>

</style>