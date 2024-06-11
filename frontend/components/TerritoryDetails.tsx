import { Territory, useAttackTerritory, useMinionMovement } from "@/hooks/useTerritories";
import { Image, StyleSheet, View, Text, Button } from "react-native";
import GoldIcon from "@/assets/icons/gold.svg";
import WoodIcon from "@/assets/icons/wood.svg";
import FoodIcon from "@/assets/icons/food.svg";
import MeepleIcon from "@/assets/icons/meeple.svg";
import { useCurrentUser } from "@/hooks/useUser";
import { useUpgradeBuilding } from "@/hooks/useBuildings";
import { useInventory } from "@/hooks/useInventory";
import { TextInput } from "react-native-gesture-handler";
import { useState } from "react";

const ICON_SIZE = 24;

export const TerritoryDetails = ({ territory }: { territory?: Territory }) => {
  const { data: user } = useCurrentUser();
  const { data: inventory } = useInventory();

  const { mutate: upgradeBuilding } = useUpgradeBuilding();
  const { mutate: attackTerritory } = useAttackTerritory();
  const { mutate: moveMinions } = useMinionMovement();

  const [dropMinions, setDropMinions] = useState(String(inventory?.MINIONS || 0));
  const [collectMinions, setCollectMinions] = useState(String(territory?.minions || 0));

  return (
    <View style={styles.container}>
      {Boolean(territory?.ownerNick) && (
        <Text style={styles.header}>Owned by {territory?.ownerNick}</Text>
      )}
      {/* <Text>{JSON.stringify(territory)}</Text> */}
      <View style={styles.detailsContainer}>
        <View style={{ flex: 1, ...styles.resourcesContainer }}>
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
        <View style={{ flex: 1, ...styles.resourcesContainer }}>
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
          {territory?.ownerNick === user?.nick ? (
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
            {territory?.ownerNick === user?.nick ? (
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
                  onPress={() => upgradeBuilding({ buildingId: building.id })}
                />
              </>
            ) : (
              <></>
            )}
          </View>
        ))}
      </View>
      {territory?.ownerNick !== user?.nick ? (
        <View style={styles.resourcesContainer}>
          <Button
            title="Attack"
            onPress={() => attackTerritory({ territoryId: territory?.id })}
          />
        </View>
      ) : (
        <View style={styles.resourcesContainer}>
          <View style={{ flexDirection: "row", alignContent: "stretch" }}>
            <TextInput
              keyboardType="number-pad"
              value={collectMinions}
              onChangeText={setCollectMinions}
              style={{ flex: 1 }}
            />
            <Button
              title="collect minions"
              onPress={() =>
                moveMinions({
                  territoryId: territory?.id,
                  collect: Number(collectMinions),
                })
              }
            />
          </View>
          <View style={{ flexDirection: "row", alignContent: "stretch" }}>
            <TextInput
              keyboardType="number-pad"
              value={dropMinions}
              onChangeText={setDropMinions}
              style={{ flex: 1 }}
            />
            <Button
              title="drop minions"
              onPress={() =>
                moveMinions({
                  territoryId: territory?.id,
                  drop: Number(dropMinions),
                })
              }
            />
          </View>
        </View>
      )}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 16,
    alignContent: "flex-start",
    alignItems: "stretch",
    justifyContent: "flex-start",
    gap: 16,
  },
  header: {
    alignContent: "center",
    fontSize: 18,
    margin: 16,
  },
  detailsContainer: {
    width: "100%",
    flexDirection: "row",
    alignItems: "stretch",
    padding: 16,
  },
  resourcesContainer: {
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
