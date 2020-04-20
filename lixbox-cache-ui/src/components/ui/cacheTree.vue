<template>
  <v-card rounded style="max-width:auto; margin-right:1rem">
    <v-sheet class="pa-4 primary lighten-2">
      <v-text-field
        v-model="search"
        :label="$t('cache.service.action.search.placeholder')"
        dark
        flat
        solo-inverted
        hide-details
        clearable
        clear-icon="mdi-close-circle-outline"
      ></v-text-field>
    </v-sheet>
    <v-card-text>
      <v-treeview
        :items="parsedItems"
        :return-object="returnObject"
        :search="search"
        activatable
        open-on-click
        open-all
        transition
        @update:active="pushActiveEvent"
      >
        <template v-slot:prepend="{ item, open }">
          <v-icon v-if="item.action != null">{{"mdi-view-grid-plus"}}</v-icon>
          <v-icon v-else-if="item.children.length > 0">
            {{
            open ? "mdi-folder-open" : "mdi-folder"
            }}
          </v-icon>
          <v-icon v-else>{{ "mdi-nodejs" }}</v-icon>
        </template>
      </v-treeview>
    </v-card-text>
  </v-card>
</template>

<script>
export default {
  name: "cacheTree",
  props: ["opens", "items"],
  data: () => ({
    active: [],
    selected: [],
    search: null,
    computedOpens: [],
    returnObject: true,
    actionsAdded: false
  }),
  computed: {
    parsedItems() {
      let result = [];
      let level = { result };

      this.items.forEach(path => {
        path.split(":").reduce((r, name) => {
          if (!r[name]) {
            r[name] = { result: [] };
            r.result.push({
              id: Math.random(),
              name: name,
              path: path,
              value: "",
              children: r[name].result
            });
          }
          return r[name];
        }, level);
      });
      result = this.addActions(result);
      return result;
    }
  },
  methods: {
    addActions(items) {
      items.push({
        name: this.$t("cache.service.action.add.text"),
        action: "addEntry"
      });
      return items;
    },
    pushActiveEvent: function(event) {
      if (event.length > 0) {
        if (event[0].action == "addEntry") {
          this.$emit("update:active", [
            { id: "", name: "", version: "", key: "new", path: "", value: "" }
          ]);
        } else {
          this.$emit("update:active", event);
        }
      }
    }
  }
};
</script>
