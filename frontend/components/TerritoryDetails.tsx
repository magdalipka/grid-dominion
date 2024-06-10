import { Territory } from "@/hooks/useTerritories";
import { Image, StyleSheet, View, Text, Button } from "react-native";
import GoldIcon from "@/assets/icons/gold.svg";
import WoodIcon from "@/assets/icons/wood.svg";
import FoodIcon from "@/assets/icons/food.svg";
import MeepleIcon from "@/assets/icons/meeple.svg";
import { useCurrentUser } from "@/hooks/useUser";
import { useUpgradeBuilding } from "@/hooks/useBuildings";
import { useInventory } from "@/hooks/useInventory";

const ICON_SIZE = 24;

export const TerritoryDetails = ({ territory }: { territory?: Territory }) => {
  const { data: user } = useCurrentUser();
  const { data: inventory } = useInventory();

  const { mutate: upgradeBuilding } = useUpgradeBuilding();

  return (
    <View style={styles.container}>
      {Boolean(territory?.ownerNick) && (
        <Text style={styles.header}>Owned by {territory?.ownerNick}</Text>
      )}
      {/* <Text>{JSON.stringify(territory)}</Text> */}
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
      <View style={styles.resourcesContainer}>
        <View style={styles.building}>
          <Text style={{ fontWeight: "bold" }}>Type</Text>
          <Text style={{ fontWeight: "bold" }}>Level</Text>
          <Text style={{ fontWeight: "bold" }}>Bonus</Text>
          {territory?.ownerNick === user?.nick || true ? (
            <>
              <Text style={{ fontWeight: "bold" }}>Gold</Text>
              <Text style={{ fontWeight: "bold" }}>Wood</Text>
              <Text style={{ fontWeight: "bold" }}>Food</Text>
              <Text style={{ fontWeight: "bold" }}>Upgrade</Text>
            </>
          ) : (
            <></>
          )}
        </View>
        {territory?.buildings.map((building) => (
          <View key={building.id} style={styles.building}>
            <Text>{building.type}</Text>
            <Text>{building.level ?? "x"}</Text>
            <Text>{building.bonus ?? "x"}</Text>
            {territory?.ownerNick === user?.nick || true ? (
              <>
                <Text>{building.goldCost}</Text>
                <Text>{building.woodCost}</Text>
                <Text>{building.foodCost}</Text>
                <Button
                  title="buy"
                  disabled={
                    inventory?.GOLD <= building.goldCost ||
                    inventory?.WOOD <= building.woodCost ||
                    inventory?.FOOD <= building.foodCost
                  }
                />
              </>
            ) : (
              <></>
            )}
          </View>
        ))}
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
    alignContent: "space-around",
    justifyContent: "flex-start",
  },
  header: {
    alignContent: "center",
    fontSize: 18,
    margin: 16,
  },
  detailsContainer: {
    flexDirection: "row",
    padding: 16,
    width: "100%",
  },
  resourcesContainer: {
    flex: 1,
    width: "100%",
    display: "flex",
    gap: 8,
    // borderColor: "red",
    // borderWidth: 1,
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
  building: {
    width: "100%",
    flexDirection: "row",
    justifyContent: "space-between",
  },
});
