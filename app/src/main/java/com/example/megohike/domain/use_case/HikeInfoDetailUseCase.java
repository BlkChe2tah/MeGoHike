package com.example.megohike.domain.use_case;

import com.example.megohike.data.data_source.database.entities.HikeInfo;

public interface HikeInfoDetailUseCase {
    HikeInfo loadHikeInfo(int hikeId);
}
