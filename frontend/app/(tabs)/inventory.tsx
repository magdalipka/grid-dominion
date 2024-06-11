import { Image, StyleSheet, View, Text } from "react-native";

import { SafeAreaView } from "react-native-safe-area-context";
import React from "react";
import GoldIcon from "@/assets/icons/gold.svg";
import WoodIcon from "@/assets/icons/wood.svg";
import FoodIcon from "@/assets/icons/food.svg";
import MeepleIcon from "@/assets/icons/meeple.svg";
import { useInventory } from "@/hooks/useInventory";

const ICON_SIZE = 24;

export default function InventoryScreen() {
  const { data: inventory, isLoading } = useInventory();

  if (isLoading) {
    return (
      <View>
        <Text>Loading...</Text>
      </View>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.resourcesContainer}>
        <View style={styles.resource}>
          <GoldIcon width={ICON_SIZE} height={ICON_SIZE} />
          <Text style={styles.resourceText}>{inventory?.GOLD || 0}</Text>
        </View>
        <View style={styles.resource}>
          <WoodIcon width={ICON_SIZE} height={ICON_SIZE} />
          <Text style={styles.resourceText}>{inventory?.WOOD || 0}</Text>
        </View>
        <View style={styles.resource}>
          <FoodIcon width={ICON_SIZE} height={ICON_SIZE} />
          <Text style={styles.resourceText}>{inventory?.FOOD || 0}</Text>
        </View>
        <View style={styles.resource}>
          <MeepleIcon width={ICON_SIZE} height={ICON_SIZE} />
          <Text style={styles.resourceText}>{inventory?.MINIONS || 0}</Text>
        </View>
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
    width: "100%",
  },
  header: {
    alignContent: "center",
    fontSize: 18,
    margin: 16,
  },
  detailsContainer: {
    flex: 1,
    flexDirection: "row",
    padding: 16,
  },
  resourcesContainer: {
    flex: 1,
    width: "100%",
    display: "flex",
    gap: 8,
  },
  resource: {
    display: "flex",
    flexDirection: "row",
    gap: 8,
  },
  army: {
    display: "flex",
    flexDirection: "row",
    justifyContent: "flex-end",
    gap: 8,
  },
  resourceText: {
    fontSize: 18,
  },
});
