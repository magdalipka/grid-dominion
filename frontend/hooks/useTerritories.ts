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

export const useTerritories = () => {
  const query = useQuery({
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

  useEffect(() => {
    if (query.data) {
      console.log("Territories received:", query.data);
    }
  }, [query.data]);

  return query;
};


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
        await request("/territories/owner", {
          method: "POST",
          headers: {
            "content-type": "application/json",
          },
          body: JSON.stringify({ Id: territoryId }),
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
            t.id === territoryId && data.win ? { ...t, ownerNick: user?.nick } : t
          ),
        ],
        data
      );
    },
    onMutate: () => {
      client.refetchQueries({ queryKey: ["territories"] });
    },
  });
};
