import { Image, StyleSheet, View, Text } from "react-native";

import { useAuth } from "@/contexts/auth";
import { SafeAreaView } from "react-native-safe-area-context";
import MapView from "react-native-maps";
import React from "react";

import * as Location from "expo-location";

export default function HomeScreen() {
  const { user } = useAuth();

  const [location, setLocation] = React.useState<{
    longitude: number;
    latitude: number;
  } | null>(null);

  React.useEffect(() => {
    (async () => {
      const { status: foregroundPermissionStatus } =
        await Location.requestForegroundPermissionsAsync();
      const { status: backgroundPermissionStatus } =
        await Location.requestForegroundPermissionsAsync();
      await Location.enableNetworkProviderAsync();
      if (
        foregroundPermissionStatus === "granted" ||
        backgroundPermissionStatus === "granted"
      ) {
        let location = await Location.getCurrentPositionAsync({});
        setLocation(location.coords);
      }
    })();
  }, []);

  if (!location) {
    return (
      <View>
        <Text>Loading...</Text>
      </View>
    );
  }

  return (
    <SafeAreaView style={styles.container}>
      <Text>{JSON.stringify({ user })}</Text>
      <MapView
        style={styles.map}
        initialRegion={{
          ...location,
          latitudeDelta: 0.1,
          longitudeDelta: 0.05,
        }}
      />
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
