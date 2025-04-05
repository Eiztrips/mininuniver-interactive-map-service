/*
 * This file is part of mininuniver-interactive-map-service.
 *
 * Copyright (C) 2025 Eiztrips
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package org.mininuniver.interactivemap.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.mininuniver.interactivemap.utils.JsonbConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@Entity
@Table(name = "Nodes")
public class Node {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int floorId;

    @Convert(converter = JsonbConverter.class)
    @Column(name = "pos", columnDefinition = "jsonb")
    private Map<String, Object> pos;

    @Column(name = "neighbors", columnDefinition = "integer[]")
    private int[] neighbors;

}
