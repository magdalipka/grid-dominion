import { Image, StyleSheet, View, Text } from "react-native";

import { useAuth } from "@/contexts/auth";
import { SafeAreaView } from "react-native-safe-area-context";
import React from "react";

import { useInventory } from "@/hooks/useInventory";

export default function InventoryScreen() {
  const { user } = useAuth();

  const { data } = useInventory();

  return (
    <SafeAreaView style={styles.container}>
      <Text>{JSON.stringify({ data })}</Text>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  map: {
    width: "100%",
    height: "100%",
  },
});
