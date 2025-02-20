import { useQuery } from 'react-query';

import SpaceCard from '@/components/user/SpaceCard';

import apis from '@/apis';

import DEFAULT_IMAGE from '@/assets/defaultSpaceImage.webp';
import logo from '@/assets/logoTitle.png';
import logoWebp from '@/assets/logoTitle.webp';

import styles from './styles';

const SpaceList: React.FC = () => {
  const { data: spaceData } = useQuery(['spaces'], apis.getSpaces);

  return (
    <div css={styles.layout}>
      <img css={styles.logo} srcSet={`${logoWebp}`} src={logo} alt="공간 체크" />
      <span css={styles.text}>현재 머무르는 공간을 선택해주세요.</span>
      {spaceData?.spaces.length === 0 ? (
        <div css={styles.empty}>관리자가 생성한 공간 없어요</div>
      ) : (
        spaceData?.spaces.map(space => (
          <SpaceCard spaceName={space.name} imageUrl={space.imageUrl || DEFAULT_IMAGE} key={space.id} id={space.id} />
        ))
      )}
      {}
    </div>
  );
};

export default SpaceList;
