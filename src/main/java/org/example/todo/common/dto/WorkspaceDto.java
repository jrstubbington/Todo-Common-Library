package org.example.todo.common.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.todo.common.util.Status;

import java.time.OffsetDateTime;
import java.util.UUID;

@NoArgsConstructor
@Data
public class WorkspaceDto implements DtoEntity {

	private  UUID uuid;

	private String name;

	private Status status;

	private int workspaceType;

	@Schema(accessMode = Schema.AccessMode.READ_ONLY)
	private OffsetDateTime dateCreated;
}
