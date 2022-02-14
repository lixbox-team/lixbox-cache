<template>
  <div>
    <confirm ref="confirm"></confirm>
    <v-row class="margin-10">
      <v-col md="3">
        <cacheTree :items="cacheEntries" @update:active="select" />
      </v-col>
      <v-col class="col-md-9 text-center">
        <v-scroll-y-transition mode="out-in">
          <div
            v-if="!selected"
            class="title grey--text text--lighten-1 font-weight-light"
            style="align-self: center;"
          >{{$t('cache.service.select')}}</div>
          <v-card v-else :key="selected.id" class="pt-6 mx-auto" flat>
            <v-card-text>
              <h3 style="text-align:center">
                <v-text-field
                  :label="$t('cache.service.entry.key')"
                  class="headline"
                  style="max-width:30%"
                  placeholder="fr:lixboxteam:lixbox:cache:key"
                  hide-details="auto"
                  v-model="selected.path"
                ></v-text-field>
              </h3>
              <div class="blue--text mb-2"></div>
              <div class="blue--text subheading font-weight-bold"></div>
            </v-card-text>
            <v-divider></v-divider>
            <v-card-text>
              <v-row class="text-align:center" tag="v-card-text">
                <v-textarea
                  v-model="jsonValue"
                  :label="$t('cache.service.entry.value')"
                  :auto-grow="true"
                  :clearable="true"
                  :filled="true"
                  :outlined="true"
                ></v-textarea>
              </v-row>
            </v-card-text>
            <v-card-actions>
              <v-btn
                color="primary"
                rounded
                outlined
                @click="save"
              >{{$t("cache.service.ui.button.entry.save")}}</v-btn>
              <v-btn
                color="error"
                rounded
                outlined
                @click="deleteServiceEntry"
              >{{$t("cache.service.ui.button.entry.del")}}</v-btn>
            </v-card-actions>
          </v-card>
        </v-scroll-y-transition>
      </v-col>
    </v-row>
  </div>
</template>

<script>
/* eslint-disable */
import axios from "axios";
import confirm from "@/components/ui/confirmDialog.vue";
import cacheTree from "@/components/ui/cacheTree.vue";
import { CacheService } from "@/api/CacheService.js";

export default {
  components: {
    cacheTree,
    confirm
  },
  name: "CacheView",
  data: () => ({
    cacheUrl: process.env.VUE_APP_CACHE_URI,
    cacheEntries: [],
    selected: null,
    dialog: false,
    new: false,
    loading: true
  }),
  created() {
    this.initialize();
  },
  computed: {
    jsonValue: {
      // getter
      get: function () {
        return JSON.stringify(this.selected.value);
      },
      // setter
      set: function (newValue) {
        this.selected.value=JSON.parse(newValue);
      }
    }
  },
  methods: {
    getCacheService() {
      return new CacheService(this.cacheUrl);
    },
    async getConfiguration() {
      axios
        .get(process.env.VUE_APP_CONFIGURATION_URI)
        .then(response => response.data)
        .then(data => {
          this.cacheUrl = data.cache;
          this.getKeys();
        })
        .catch(error =>{
          this.getKeys();
        })
        .finally(function() {
          this.loading = false;
        });
    },
    initialize() {
      this.getConfiguration();
    },
    getKeys() {
      this.getCacheService()
        .getKeys()
        .then(data => {
          this.cacheEntries = data;
        });
    },
    select(entry) {
      if (entry.length == 0) {
        this.selected = null;
      } else {
        if (entry[0].key == "new") {
          this.selected = { name: "", version: "", uris: [] };
          this.new = true;
        } else {
          this.selected = this.getCacheService()
            .get(entry[0].path)
            .then(data => {
              this.selected = entry[0];
              this.selected.value = data;
            });
        }
      }
    },
    async updateEntry(index, oldValue, newValue) {
      this.selected.uris[index] = newValue;
    },
    async deleteEntry(index) {
      if (
        await this.$refs.confirm.open(
          this.$t("cache.service.dialog.entry.uri.delete.title", [
            this.selected.name
          ]),
          this.$t("cache.service.dialog.entry.uri.delete.text"),
          {
            color: this.$vuetify.theme.themes.light.error
          }
        )
      ) {
        this.selected.uris.splice(index, 1);
      }
    },
    save() {
      this.getCacheService()
        .put(this.selected.path, this.selected.value)
        .then(data => {
          if (this.new) {
            this.cacheEntries.push(this.selected.path);
          }
        });
    },
    async deleteServiceEntry() {
      if (
        await this.$refs.confirm.open(
          this.$t("cache.service.dialog.entry.delete.title", [
            this.selected.name
          ]),
          this.$t("cache.service.dialog.entry.delete.text"),
          {
            color: this.$vuetify.theme.themes.light.error
          }
        )
      ) {
        this.getCacheService()
          .remove(this.selected.path)
          .then(data => {
            if (data) {
              this.cacheEntries.splice(
                this.cacheEntries.indexOf(this.selected.path),
                1
              );
            }
          });
      }
    }
  }
};
</script>

<style scoped>
.margin-5 {
  margin: 0.5rem 0.5rem 0 0.5rem;
}
.margin-10 {
  margin: 1rem 1rem 0 1rem;
}
</style>
