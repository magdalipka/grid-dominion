import { request } from "@/lib/request";
import { useQuery } from "@tanstack/react-query";
import { useEffect } from "react";

export type Territory = {
  id: number;
  maxLatitude: number;
  minLatitude: number;
  maxLongitude: number;
  minLongitude: number;
  gold: number;
  wood: number;
  food: number;
  ownerNick: string;
};

export const useTerritories = () =>
  useQuery({
    queryKey: ["territories"],
    queryFn: async () => {
      const res = (await (
        await request("/territories", {
          method: "GET",
        })
      ).json()) as Array<Territory>;
      return res;
    },
  });

export const useVisibleTerritories = ({
  latitude,
  longitude,
  radius,
}: {
  latitude?: number;
  longitude?: number;
  radius: number;
}) => {
  const { data: territories, isLoading } = useTerritories();

  useEffect(() => {
    console.log({ isLoading, territories: territories?.length });
  }, [isLoading, territories]);

  if (!latitude || !longitude || !radius) {
    return { isLoading: true, territories: [] };
  }

  return {
    territories: (territories || []).filter(
      (t) =>
        t.maxLatitude < latitude + radius &&
        t.minLatitude > latitude - radius &&
        t.maxLongitude < longitude + radius &&
        t.minLongitude > longitude - radius
    ),
    isLoading,
  };
};

export const useCurrentTerritory = ({
  latitude,
  longitude,
}: {
  latitude?: number;
  longitude?: number;
}) => {
  const { data: territories, isLoading } = useTerritories();

  if (!latitude || !longitude) {
    return { isLoading: true, territory: undefined };
  }
  return {
    isLoading,
    territory: territories?.find(
      (t) =>
        t.maxLatitude > latitude &&
        t.minLatitude < latitude &&
        t.maxLongitude > longitude &&
        t.minLongitude < longitude
    ),
  };
};
