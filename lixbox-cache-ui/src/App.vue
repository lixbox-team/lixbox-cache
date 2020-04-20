<template>
  <v-app>
    <menuGauche v-model="drawer"></menuGauche>
    <v-app-bar app clipped-left dark>
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"></v-app-bar-nav-icon>
      <v-toolbar-title>{{ $t('cache.application.title', [currentYear]) }}</v-toolbar-title>
    </v-app-bar>

    <v-content>
      <router-view></router-view>
    </v-content>
    <v-footer color="darken-1" style="margin-top: 15px; font-size:0.9rem">
      <v-layout row wrap align-right>
        <v-flex xs12>
          <div
            class="black--text"
            style="text-align:right;"
          >{{ $t('cache.application.copyright', [currentYear]) }}</div>
        </v-flex>
      </v-layout>
    </v-footer>
  </v-app>
</template>

<script>
import menuGauche from "@/components/navigation/menugauche";

export default {
  components: {
    menuGauche
  },
  props: {
    source: String
  },
  data: () => ({
    drawer: true
  }),
  created() {
    this.$vuetify.theme.dark = false;
  },
  computed: {
    currentYear: function() {
      return new Date().getFullYear();
    }
  }
};
</script>