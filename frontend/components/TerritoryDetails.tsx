import { Territory } from "@/hooks/useTerritories";
import { Image, StyleSheet, View, Text, Button } from "react-native";
import GoldIcon from "@/assets/icons/gold.svg";
import WoodIcon from "@/assets/icons/wood.svg";
import FoodIcon from "@/assets/icons/food.svg";
import MeepleIcon from "@/assets/icons/meeple.svg";

const ICON_SIZE = 24;

export const TerritoryDetails = ({ territory }: { territory?: Territory }) => {
  return (
    <View style={styles.container}>
      {Boolean(territory?.ownerNick) && (
        <Text style={styles.header}>Owned by {territory?.ownerNick}</Text>
      )}
      <Text>{JSON.stringify(territory)}</Text>
      <View style={styles.detailsContainer}>
        <View style={styles.resourcesContainer}>
          <View style={styles.resource}>
            <GoldIcon width={ICON_SIZE} height={ICON_SIZE} />
            <Text style={styles.resourceText}>{territory?.gold || 0}</Text>
          </View>
          <View style={styles.resource}>
            <WoodIcon width={ICON_SIZE} height={ICON_SIZE} />
            <Text style={styles.resourceText}>{territory?.wood || 0}</Text>
          </View>
          <View style={styles.resource}>
            <FoodIcon width={ICON_SIZE} height={ICON_SIZE} />
            <Text style={styles.resourceText}>{territory?.food || 0}</Text>
          </View>
        </View>
        <View style={styles.resourcesContainer}>
          <View style={styles.army}>
            <Text style={styles.resourceText}>{territory?.minions || 0}</Text>
            <MeepleIcon width={ICON_SIZE} height={ICON_SIZE} />
          </View>
        </View>
      </View>
    </View>
  );
};

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
