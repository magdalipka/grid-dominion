import { Image, StyleSheet, View, Text, Button } from "react-native";

import { useAuth } from "@/contexts/auth";
import MapView, { Marker, Polygon } from "react-native-maps";
import React, { useCallback, useRef, useState } from "react";
import Ionicons from "@expo/vector-icons/Ionicons";

import * as Location from "expo-location";
import {
  Territory,
  useCurrentTerritory,
  useVisibleTerritories,
} from "@/hooks/useTerritories";
import { BottomSheetModal, BottomSheetView } from "@gorhom/bottom-sheet";
import { TerritoryDetails } from "@/components/TerritoryDetails";
// import * as TaskManager from "expo-task-manager";

// const LOCATION_TASK_NAME = "griddominion-update-location";

// TaskManager.defineTask(LOCATION_TASK_NAME, async ({ data, error }) => {
//   console.log({ data, error });
//   if (error) {
//     return;
//   }
//   if (data) {
//     const { locations } = data;
//     const res = await (
//       await request("/users", {
//         method: "POST",
//         headers: {
//           "content-type": "application/json",
//         },
//         body: JSON.stringify(locations),
//       })
//     ).json();
//     console.log({ res });
//   }
// });

export default function HomeScreen() {
  const { user } = useAuth();

  const [location, setLocation] = React.useState<{
    longitude: number;
    latitude: number;
  } | null>(null);

  const bottomSheetModalRef = useRef<BottomSheetModal>(null);

  React.useEffect(() => {
    (async () => {
      await Location.enableNetworkProviderAsync();
      const { status: foregroundPermissionStatus } =
        await Location.requestForegroundPermissionsAsync();
      const { status: backgroundPermissionStatus } =
        await Location.requestBackgroundPermissionsAsync();
      if (
        foregroundPermissionStatus === "granted" ||
        backgroundPermissionStatus === "granted"
      ) {
        let location = await Location.getCurrentPositionAsync({});
        setLocation(location.coords);
        // await Location.startLocationUpdatesAsync(LOCATION_TASK_NAME, {
        //   accuracy: Location.Accuracy.Highest,
        //   distanceInterval: 0,
        //   timeInterval: 5,
        // });
      }
    })();
  }, []);

  const handlePresentModalPress = useCallback(() => {
    bottomSheetModalRef.current?.present();
  }, []);
  const handleCloseModal = useCallback(() => {
    setSelectedTerritory(undefined);
  }, []);

  const { territories, isLoading } = useVisibleTerritories({
    ...location,
    radius: 0.04,
  });

  const { territory: currentTerritory } = useCurrentTerritory({ ...location });

  const [selectedTerritory, setSelectedTerritory] = useState<Territory>();

  if (!location || isLoading) {
    return (
      <View>
        <Text>Loading...</Text>
      </View>
    );
  }

  return (
    <>
      <MapView
        style={styles.map}
        initialRegion={{
          ...location,
          latitudeDelta: 0.05,
          longitudeDelta: 0.05,
        }}
        showsUserLocation
        followsUserLocation
        minZoomLevel={13}
      >
        {territories.map((t) => (
          <Polygon
            key={t.id}
            coordinates={[
              { latitude: t.maxLatitude, longitude: t.maxLongitude },
              { latitude: t.maxLatitude, longitude: t.minLongitude },
              { latitude: t.minLatitude, longitude: t.minLongitude },
              { latitude: t.minLatitude, longitude: t.maxLongitude },
            ]}
            fillColor="#00005555"
            strokeWidth={
              t.id === selectedTerritory?.id ? 1 : t.id === currentTerritory?.id ? 0.1 : 0
            }
            tappable
            onPress={() => {
              setSelectedTerritory(t);
              handlePresentModalPress();
            }}
          />
        ))}
      </MapView>
      <BottomSheetModal
        ref={bottomSheetModalRef}
        snapPoints={["50%"]}
        // style={styles.container}
        enableDynamicSizing
        enablePanDownToClose
        enableDismissOnClose
        onDismiss={handleCloseModal}
      >
        <BottomSheetView style={bottomSheetStyles.contentContainer}>
          <TerritoryDetails territory={selectedTerritory} />
        </BottomSheetView>
      </BottomSheetModal>
    </>
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

const bottomSheetStyles = StyleSheet.create({
  contentContainer: {
    flex: 1,
    alignItems: "center",
  },
});
