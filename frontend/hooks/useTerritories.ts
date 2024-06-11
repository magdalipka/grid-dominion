import { request } from "@/lib/request";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import { useEffect } from "react";
import { useCurrentUser } from "./useUser";

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
  minions: number;
  buildings: Array<{
    id: string;
    level: number;
    goldCost: number;
    woodCost: number;
    foodCost: number;
    bonus: number;
    type: string;
  }>;
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

export const useAttackTerritory = () => {
  const client = useQueryClient();
  const { data: user } = useCurrentUser();

  return useMutation({
    mutationFn: async ({ territoryId }: { territoryId: number }) => {
      const res = await (
        await request("/territories/" + territoryId + "/invade", {
          method: "POST",
          headers: {
            "content-type": "application/json",
          },
        })
      ).json();
      console.log({ res });
      return res;
    },
    onSuccess: (data, { territoryId }) => {
      console.log({ data, territoryId, user });
      client.setQueryData(
        ["territories"],
        (oldData: Array<Territory>) => [
          ...oldData!.map((t) =>
            t.id === territoryId
              ? {
                  ...t,
                  ...(data.win ? { ownerNick: user?.nick } : {}),
                  minions: data.territoryMinions,
                }
              : t
          ),
        ],
        data
      );
    },
    onMutate: () => {
      client.refetchQueries({ queryKey: ["territories"] });
      client.refetchQueries({ queryKey: ["myInventory"] });
    },
  });
};

export const useMinionMovement = () => {
  const client = useQueryClient();
  const { data: user } = useCurrentUser();

  return useMutation({
    mutationFn: async ({
      territoryId,
      drop = 0,
      collect = 0,
    }: {
      territoryId: number;
      drop?: number;
      collect?: number;
    }) => {
      console.log({ territoryId, drop, collect });
      const res = await (
        await request("/territories/" + territoryId + "/minions", {
          method: "POST",
          headers: {
            "content-type": "application/json",
          },
          body: JSON.stringify({
            drop: drop,
            collect: collect,
          }),
        })
      ).json();
      console.log({ res });
      return res;
    },
    onSuccess: (data, { territoryId, collect = 0, drop = 0 }) => {
      console.log({ data, territoryId, user });
      client.setQueryData(
        ["territories"],
        (oldData: Array<Territory>) => [
          ...oldData!.map((t) =>
            t.id === territoryId ? { ...t, minions: t.minions - collect + drop } : t
          ),
        ],
        data
      );
    },
    onMutate: () => {
      client.refetchQueries({ queryKey: ["territories"] });
      client.refetchQueries({ queryKey: ["myInventory"] });
    },
  });
};
