package io.iqpizza.starcraft.simcity;

import com.github.ocraft.s2client.protocol.spatial.RectangleI;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static io.iqpizza.starcraft.simcity.TileType.*;

@Slf4j
public class GameMap {
    @Getter
    private final int fullWidth;
    @Getter
    private final int fullHeight;
    @Getter
    private final TileType[][] tiles;
    private final RectangleI playableArea;

    public GameMap(int fullWidth, int fullHeight, RectangleI playableArea) {
        log.info("Map({}, {})", fullWidth, fullHeight);
        this.fullWidth = fullWidth;
        this.fullHeight = fullHeight;
        this.playableArea = playableArea;
        this.tiles = new TileType[fullHeight][fullWidth];
        initializeMap();
    }

    private void initializeMap() {
        for (int y = 0; y < fullHeight; y++) {
            for (int x = 0; x < fullWidth; x++) {
                if (isPlayableArea(x, y)) {
                    tiles[y][x] = EMPTY;
                } else {
                    tiles[y][x] = INVALID;
                }
            }
        }
    }

    // 특정 좌표가 플레이 가능한 영역에 있는지 확인
    private boolean isPlayableArea(int x, int y) {
        return x >= playableArea.getP0().getX() && x < playableArea.getP1().getX() &&
                y >= playableArea.getP0().getY() && y < playableArea.getP1().getY();
    }

    public boolean canPlaceBuilding(int x, int y, Size size) {
        if (isBuildable(x, y, size)) {
            return false;
        }

        for (int i = 0; i < size.height(); i++) {
            for (int j = 0; j < size.width(); j++) {
                if (tiles[y + i][x + j] != EMPTY) {
                    return false; // 이미 다른 건물이 있는 경우
                }
            }
        }
        return true;
    }

    public boolean isBuildable(int x, int y, Size size) {
        return !(x + size.width() > fullWidth || y + size.height() > fullHeight) && !isTileInValid(x + size.width(), y + size.height());
    }

    public boolean isTileEmpty(int x, int y) {
        return tiles[y][x] == EMPTY;
    }

    private boolean isTileInValid(int x, int y) {
        return tiles[y][x] == INVALID;
    }

    // 건물 배치
    public boolean placeBuilding(int x, int y, Building building) {
        if (!canPlaceBuilding(x, y, building.size())) {
            return false; // 건물을 배치할 수 없는 경우
        }

        // 건물을 배치하고 타일 상태를 갱신
        for (int i = 0; i < building.size().height(); i++) {
            for (int j = 0; j < building.size().width(); j++) {
                tiles[y + i][x + j] = BUILDING;
            }
        }
        return true;
    }

    // 건물 파괴
    public void destroyBuilding(int x, int y, Size buildingSize) {
        for (int i = 0; i < buildingSize.height(); i++) {
            for (int j = 0; j < buildingSize.width(); j++) {
                tiles[y + i][x + j] = TileType.BUILDING_DESTROYED;
            }
        }
    }

    public void updateMineralField(int x, int y, boolean hasMinerals) {
        if (hasMinerals) {
            tiles[y][x] = TileType.MINERAL;
        } else {
            tiles[y][x] = TileType.EMPTY;
        }
    }

    public void updateVespeneGas(int x, int y, boolean hasVespene) {
        if (hasVespene) {
            tiles[y][x] = TileType.GAS;
        } else {
            tiles[y][x] = TileType.EMPTY;
        }
    }

    public void printMap() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");

        for (int y = 0; y < fullHeight; y++) {
            for (int x = 0; x < fullWidth; x++) {
                switch (tiles[y][x]) {
                    case INVALID -> sb.append("■");
                    case EMPTY -> sb.append(".");
                    case BUILDING -> sb.append("B");
                    case MINERAL -> sb.append("M");
                    case GAS -> sb.append("G");
                    case BUILDING_DESTROYED -> sb.append("X");
                    case DESTRUCTIBLE_ROCK -> sb.append("#");
                }
            }
            sb.append("\n");
        }

        String mapString = sb.toString();
        log.debug(mapString.replace("■", "").trim());
    }

    public void updateDestructableStructure(int x, int y, boolean hasDestructable) {
        if (hasDestructable) {
            tiles[y][x] = DESTRUCTIBLE_ROCK;
        } else {
            tiles[y][x] = EMPTY;
        }
    }
}
