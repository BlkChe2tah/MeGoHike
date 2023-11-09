package com.example.megohike.domain.use_case;

import com.example.megohike.data.data_source.database.entities.HikeInfo;

public interface DeleteHikeInfoUseCase {
    void deleteHikeInfo(int hikeId);
}
