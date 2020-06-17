<template>
  <div class="ma-5">
    <v-toolbar flat color="white">
      <v-toolbar-title>Stocks</v-toolbar-title>
    </v-toolbar>
    <v-toolbar flat color="white">
      <!-- <v-btn round  color="info" @click="back()"><v-icon small>arrow_back_ios</v-icon>Back</v-btn> -->
      <v-spacer></v-spacer>
      <v-text-field v-model="search" append-icon="search" label="Search" single-line hide-details></v-text-field>
      <v-dialog v-model="dialog" max-width="500px">
        <v-btn slot="activator" color="info" dark class="mb-2">New Stock Data</v-btn>
        <v-card v-if="uploadCSV == false">
          <v-card-title>
            <span class="headline">{{ formTitle }}</span>
          </v-card-title>
          <v-card-text>
            <v-container grid-list-md>
              <v-layout wrap>
                <v-flex xs12 sm6 md4>
                  <v-text-field v-if="transferItemIndex == -1 && editedIndex == -1" v-model="editedItem.location" label="Location" required></v-text-field>
                  <v-text-field v-if="transferItemIndex == -1 && editedIndex != -1" v-model="editedItem.location" label="Location" required disabled></v-text-field>
                  <v-text-field v-if="transferItemIndex != -1" v-model="transferItem.formLocation" label="Location" required disabled></v-text-field>
                </v-flex>
                <v-flex xs12 sm6 md4>
                  <v-text-field v-if="transferItemIndex == -1 && editedIndex == -1" v-model="editedItem.code" label="Code"  required></v-text-field>
                  <v-text-field v-if="transferItemIndex == -1 && editedIndex != -1" v-model="editedItem.code" label="Location" required disabled></v-text-field>
                  <v-text-field v-if="transferItemIndex != -1" v-model="transferItem.code" label="Code" required disabled></v-text-field>
                </v-flex>
                <v-flex xs12 sm6 md4>
                  <v-text-field v-if="transferItemIndex == -1" v-model="editedItem.quantity" label="Quantity"  required></v-text-field>
                  <v-text-field v-if="transferItemIndex != -1" v-model="transferItem.formQuantity" label="Quantity" required disabled></v-text-field>
                </v-flex>
              </v-layout>
            </v-container>
            <v-container grid-list-md  v-if="transferItemIndex != -1" >
              <v-layout wrap>
                <v-flex xs12 sm6 md4>
                  <v-text-field v-if="transferItemIndex != -1" v-model="transferItem.toLocation" label="To Location" required></v-text-field>
                </v-flex>
                <v-flex xs12 sm6 md4>
                  <v-text-field v-if="transferItemIndex != -1" v-model="transferItem.toQuantity" label="Transfer Quantity"  required></v-text-field>
                </v-flex>
              </v-layout>
            </v-container>
          </v-card-text>

          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" flat @click="close">Cancel</v-btn>
            <v-btn color="blue darken-1" flat @click="save">Save</v-btn>
          </v-card-actions>
        </v-card >
        <v-card v-if="uploadCSV == true">
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
      <v-btn color="info" dark class="mb-2" @click="toUploadCSV">Upload CSV</v-btn>
      <v-btn color="info" dark class="mb-2" @click="download">Download as CSV</v-btn>
    </v-toolbar>
    <v-data-table :headers="headers" :items="stocks" :search="search" class="elevation-1" >
      <template slot="items" slot-scope="props">

        <td class="text-xs-left">{{ props.item.location }}</td>
        <td class="text-xs-left">{{ props.item.code }}</td>
        <td class="text-xs-left">{{ props.item.quantity }}</td>

        <td class="layout px-4">
          <v-icon  small  class="mr-2" @click="editItem(props.item)" > edit </v-icon>
          <v-icon  small  class="mr-2" @click="deleteItem(props.item)" >  delete  </v-icon>
          <v-icon  small  class="mr-2" @click="toTransferItem(props.item)" > commute  </v-icon>
        </td>


      </template>
    </v-data-table>
  </div>
</template>

<script>
import axios from 'axios'
  export default {
    data: () => ({
      dialog: false,
      search:'',
      uploadCSV:false,
      headers: [
          { text: 'Location ',align: 'left',value: 'location'},
          { text: 'Code ',align: 'left',value: 'code'},
          { text: 'Quantity', value: 'quantity' },
          { text: 'Action', value: 'name', sortable:false }
        ],
      editedIndex: -1,
      transferItemIndex: -1,
      editedItem: {
        location:'', code: '', quantity:'', 
      },
      transferItem:{
        formLocation:'', toLocation:'', code: '', formQuantity:0, toQuantity:0, 
      },
      defaultItem: {
        location: '', code: '', quantity:0,
      },
      defaultTrasnferItem:{
        formLocation:'', toLocation:'', code: '', formQuantity:0, toQuantity:'', 
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
      editItem (item) {
        this.editedIndex = this.stocks.indexOf(item)
        this.editedItem = Object.assign({}, item)
        this.dialog = true
      },
      toTransferItem(item){
        console.log('testing')
        this.transferItemIndex = this.stocks.indexOf(item)
        console.log(this.transferItemIndex)
        this.transferItem.formLocation = item.location
        this.transferItem.code = item.code
        this.transferItem.formQuantity = item.quantity
        this.dialog = true
      },
      toUploadCSV(){
        this.uploadCSV = true;
        this.dialog = true;
      },
      deleteItem (item) {
        const index = this.stocks.indexOf(item)
        var url = "http://localhost:8080/api/stocks/"+ item.code+ "/"+ item.location
        var item_info = "Location: "+item.location+"\n"+ "Code: "+item.code+ "Quantity"+ item.quantity+"\n"
        if(confirm('Are you sure you want to delete the following item?\n'+item_info)){
          axios.delete(url)
            .then(function (response) {
              alert("Deleted")
              this.$store.dispatch('loadStocks')
            }.bind(this))
            .catch(function (error) {
              alert(error);
            });
       } 
      },

      close () {
        this.dialog = false
        setTimeout(() => {
          this.editedItem = Object.assign({}, this.defaultItem)
          this.transferItem = Object.assign({}, this.defaultTrasnferItem)
          this.editedIndex = -1
          this.transferItemIndex = -1
          this.uploadCSV = false
        }, 300)
      },

      save () {
        if (this.editedIndex > -1) {
          axios.put('http://localhost:8080/api/stocks/updateQt/'+this.editedItem.code+ "/"+ this.editedItem.location, this.editedItem)
            .then(function (response) {
              alert("Updated");
              this.$store.dispatch('loadStocks')
            }.bind(this))
            .catch(function (error) {
              console.log(error);
            });
        } else if (this.transferItemIndex > -1){
          axios.put('http://localhost:8080/api/stocks/'+this.transferItem.code+ "/"+ this.transferItem.formLocation+ "/" +this.transferItem.toLocation+ "/"+ this.transferItem.toQuantity)
            .then(function (response) {
              alert("Transfered");
              this.$store.dispatch('loadStocks')
            }.bind(this))
            .catch(function (error) {
              console.log(error);
            });
        } else {
          axios.post('http://localhost:8080/api/stocks/', this.editedItem)
            .then(function (response) {
              alert("Created");
              this.$store.dispatch('loadStocks')
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
          axios.post( 'http://localhost:8080/api/stocks/upload', formData, headers );
        }catch(err){
          console.log(err)
        }finally{
          alert("uploaded")
          setTimeout(function(){ this.$store.dispatch('loadStocks') }.bind(this), 2000);
        }
        this.close()
      },
      download(){
        axios({
          url: 'http://localhost:8080/api/stocks/download',
          method: 'GET',
          responseType: 'blob',
        }).then((response) => {
          const url = window.URL.createObjectURL(new Blob([response.data]));
          const link = document.createElement('a');
          link.href = url;
          link.setAttribute('download', 'stocks.csv');
          document.body.appendChild(link);
          link.click();
        });
      },
    },
    
    computed: {
      formTitle () {
        if (this.editedIndex == -1 && this.transferItemIndex == -1)
          return 'New Stock Data'
        else if (this.transferItemIndex != -1)
          return 'Transfer to Other Stock'
        else if (this.editedIndex != -1)
          return 'Edit Stock Data' 
      },
      stocks(){
        return this.$store.state.allStocks
      }
    },
  }
</script>
<style scoped>

</style>