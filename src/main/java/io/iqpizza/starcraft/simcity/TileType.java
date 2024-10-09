package io.iqpizza.starcraft.simcity;

public enum TileType {
    INVALID,    // 공중, 벽, 물 등 원래부터 정적으로 건물을 지을 수 없는 공간
    EMPTY,
    BUILDING,
    MINERAL,
    GAS,
    DESTRUCTIBLE_ROCK,  // 추가 상태: 파괴 가능한 지형
    BUILDING_DESTROYED  //추가 상태: 파괴된 건물
}
