<template>
  <v-navigation-drawer expand-on-hover :mini-variant="isCollapsed()" v-model="cValue" app clipped color="#FEFEFA">
    <v-list dense>
      <v-list-item :to="{ name: 'cache' }" active-class>
        <v-list-item-action>
          <v-icon>mdi-database</v-icon>
        </v-list-item-action>
        <v-list-item-content>
          <v-list-item-title>{{ $t('cache.application.menu.registry')}}</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
    </v-list>
    <template v-slot:append>
      <div class="pa-2">
        <v-img src="@/assets/images/logo.png" class="logo" />
      </div>
    </template>
  </v-navigation-drawer>
</template>

<script>
export default {
  name: "menuGauche",
  inheritAttrs: false,
  props: ["label", "value"],
  computed: {
    cValue() {
      return this.value;
    }
  },
  methods: {
    isCollapsed() {
      return true;
    }
  }
}
</script>

<style scoped>
  .logo{
    max-height:70px; 
    max-width:70px; 
    margin-bottom:5px;
  }
</style>
