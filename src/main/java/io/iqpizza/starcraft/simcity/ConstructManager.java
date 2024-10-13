package io.iqpizza.starcraft.simcity;

import com.github.ocraft.s2client.protocol.spatial.Point2d;
import io.iqpizza.starcraft.GameManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("squid:S6548")
public class ConstructManager {
    private static ConstructManager instance;
    // 자신의 건물 위치를 저장하는 리스트
    private final List<Point2d> friendlyBuildings;

    private ConstructManager() {
        this.friendlyBuildings = new ArrayList<>();
    }

    public static ConstructManager getInstance() {
        if (instance == null) {
            instance = new ConstructManager();
        }
        return instance;
    }

    public void clearBuildings() {
        friendlyBuildings.clear();
    }

    // 리파이너리 건설이 가능한 위치를 찾는 메서드
    public Optional<Point2d> findRefineryLocation(Point2d selfPosition) {
        GameMap gameMap = GameManager.getInstance().getGameMap();
        TileType[][] tiles = gameMap.getTiles();
        int width = gameMap.getFullWidth();
        int height = gameMap.getFullHeight();

        List<Point2d> buildableLocations = new ArrayList<>();

        // 맵을 순회하여 가스 간헐천(GAS) 타일을 찾음
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (tiles[y][x] == TileType.GAS) {
                    Point2d gasPosition = Point2d.of(x, y);
                    // 주변에 자신의 건물이 있는지 확인
                    if (hasFriendlyBuildingNearby(gasPosition)) {
                        buildableLocations.add(gasPosition);
                    }
                }
            }
        }

        // 건설 가능한 위치가 없다면 빈 Optional 반환
        if (buildableLocations.isEmpty()) {
            return Optional.empty();
        }

        // 가장 가까운 위치를 선택
        return Optional.ofNullable(findClosestLocation(selfPosition, buildableLocations));
    }

    // 특정 위치에서 주변에 자신의 건물이 있는지 확인하는 메서드
    private boolean hasFriendlyBuildingNearby(Point2d position) {
        int range = 5; // 주변을 확인할 거리 (예시로 5타일 내외)

        for (Point2d building : friendlyBuildings) {
            double distance = position.distance(building);
            if (distance <= range) {
                return true;
            }
        }
        return false;
    }

    // 주어진 위치 목록에서 가장 가까운 위치를 찾는 메서드
    private Point2d findClosestLocation(Point2d selfPosition, List<Point2d> locations) {
        Point2d closestLocation = null;
        double minDistance = Double.MAX_VALUE;

        for (Point2d location : locations) {
            double distance = selfPosition.distance(location);
            if (distance < minDistance) {
                minDistance = distance;
                closestLocation = location;
            }
        }

        return closestLocation;
    }

    // 건물 위치를 추가하는 메서드 (건물이 지어졌을 때 호출)
    public void addFriendlyBuilding(Point2d buildingPosition) {
        friendlyBuildings.add(buildingPosition);
    }

    // 건물 위치를 제거하는 메서드 (건물이 파괴되었을 때 호출)
    public void removeFriendlyBuilding(Point2d buildingPosition) {
        friendlyBuildings.remove(buildingPosition);
    }

    public List<Point2d> getFriendlyBuildings() {
        return Collections.unmodifiableList(friendlyBuildings);
    }

}
